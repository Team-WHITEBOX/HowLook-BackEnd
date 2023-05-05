package org.whitebox.howlook.global.config.security;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.member.service.CustomOAuth2UserService;
import org.whitebox.howlook.domain.member.service.CustomUserDetailsService;
import org.whitebox.howlook.global.config.security.filter.APILoginFilter;
import org.whitebox.howlook.global.config.security.filter.RefreshTokenFilter;
import org.whitebox.howlook.global.config.security.filter.TokenCheckFilter;
import org.whitebox.howlook.global.config.security.handler.APILoginSuccessHandler;
import org.whitebox.howlook.global.config.security.handler.Custom403Handler;
import org.whitebox.howlook.global.config.security.handler.CustomAuthenticationFailureHandler;
import org.whitebox.howlook.global.config.security.handler.CustomSocialLoginSuccessHandler;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.ErrorResponse;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;

import static org.whitebox.howlook.global.error.ErrorCode.JWT_UNACCEPT;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final MemberRepository memberRepository;
    private final RedisTemplate redisTemplate;
    private static final String[] WHITELIST = {"/account/**","/swagger*/**","/v3/api-docs","/api/v2/**","/ws**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        log.info("---------------configure-----------------");
        //authenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);
        http.httpBasic().disable();
        http.formLogin().disable();
        http.logout().disable();
        http.csrf().disable();  // csrf 비활성화
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // JWT위해 세션 사용안함
        http.authorizeRequests()
                .antMatchers(WHITELIST).permitAll()
                .antMatchers("/sample/doB").hasAnyRole("ADMIN")
                .antMatchers("/member/**").hasAnyRole("USER")
                .anyRequest().hasAnyRole("USER")
                .and()
                .addFilterBefore(apiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenCheckFilter(jwtUtil,WHITELIST), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(refreshTokenCheckFilter("/account/refreshToken", jwtUtil), TokenCheckFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()); // 403
        http.cors(httpSecurityCorsConfigurer -> {         //cors문제 해결
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });
        http.oauth2Login().userInfoEndpoint().userService(customOAuth2UserService()).and().successHandler(authenticationSuccessHandler());
        return http.build();
    }

    @NotNull
    private RefreshTokenFilter refreshTokenCheckFilter(String path,JWTUtil jwtUtil) {
        RefreshTokenFilter refreshTokenFilter = new RefreshTokenFilter(path,jwtUtil,redisTemplate,memberRepository);
        return refreshTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomSocialLoginSuccessHandler(jwtUtil,redisTemplate);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

    private APILoginFilter apiLoginFilter(AuthenticationManager authenticationManager){
        //APILoginFilter
        APILoginFilter apiLoginFilter = new APILoginFilter("/account/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        //APILoginSuccessHandler
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil,redisTemplate);
        //핸들러 세팅
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);
        apiLoginFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return apiLoginFilter;
    }

    public CustomOAuth2UserService customOAuth2UserService(){
        return new CustomOAuth2UserService(memberRepository,passwordEncoder());
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("-----------web configure---------");
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil,String[] whiteList){
        return new TokenCheckFilter(jwtUtil,whiteList);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
