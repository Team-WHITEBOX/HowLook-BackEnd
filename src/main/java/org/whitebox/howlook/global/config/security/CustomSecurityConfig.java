package org.whitebox.howlook.global.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.member.service.CustomUserDetailsService;
import org.whitebox.howlook.global.config.security.filter.APILoginFilter;
import org.whitebox.howlook.global.config.security.filter.RefreshTokenFilter;
import org.whitebox.howlook.global.config.security.filter.TokenCheckFilter;
import org.whitebox.howlook.global.config.security.handler.APILoginSuccessHandler;
import org.whitebox.howlook.global.config.security.handler.Custom403Handler;
import org.whitebox.howlook.global.config.security.handler.CustomAuthenticationFailureHandler;
import org.whitebox.howlook.global.util.JWTUtil;

import java.util.Arrays;

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
//    private static final String[] WHITELIST = {"/**","/swagger*/**","/v3/api-docs","/api/v2/**","/ws**"}; // 테스트용


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
        //http.oauth2Login();
        http.cors(httpSecurityCorsConfigurer -> {         //cors문제 해결
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });
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
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("-----------web configure---------");
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil,String[] whiteList){
        return new TokenCheckFilter(jwtUtil,whiteList,redisTemplate);
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