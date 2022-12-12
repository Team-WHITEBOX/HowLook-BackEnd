package org.whitebox.howlook.global.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.whitebox.howlook.global.util.JWTUtil;

import javax.sql.DataSource;
import java.util.Arrays;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final MemberRepository memberRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomSocialLoginSuccessHandler(passwordEncoder(),jwtUtil);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        log.info("---------------configure-----------------");
//        http.formLogin().loginPage("/member/login").usernameParameter("loginId").passwordParameter("password");
        //       .loginProcessingUrl("/api/login")  login action url
        //        .usernameParameter("loginId")    default : username
        //        .passwordParameter("password")

        //authenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        //Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //APILoginFilter
        APILoginFilter apiLoginFilter = new APILoginFilter("/account/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        //APILoginSuccessHandler
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil,memberRepository);
        //핸들러 세팅
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);
        apiLoginFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        //반드시 필요
        http.authenticationManager(authenticationManager);

        //APILoginFilter 위치조정
//        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        //api로 시작하는 모든경로 토큰체크필터 동작
//        http.addFilterBefore(tokenCheckFilter(jwtUtil,userDetailsService), UsernamePasswordAuthenticationFilter.class);

        //refreshToken 호출처리
  //      http.addFilterBefore(new RefreshTokenFilter("/refreshToken",jwtUtil), TokenCheckFilter.class);


        http.httpBasic().disable();
        http.formLogin().disable();
        http.csrf().disable();  // csrf 비활성화
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // JWT위해 세션 사용안함

        http.authorizeRequests()
             //   .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()  //cors preflight 요청 통과
                .antMatchers("/account/**","/swagger*/**","/v3/api-docs","/api/v2/**").permitAll()
                .antMatchers("/sample/doB").hasAnyRole("ADMIN")
                .antMatchers("/sample/doA","/member/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenCheckFilter(jwtUtil,userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RefreshTokenFilter("/account/refreshToken",jwtUtil), TokenCheckFilter.class);

        http.cors(httpSecurityCorsConfigurer -> {         //cors문제 해결
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

//        http.rememberMe().key("12345678").tokenRepository(persistentTokenRepository()).userDetailsService(userDetailsService).tokenValiditySeconds(60*60*24*30);

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()); // 403

        http.oauth2Login().userInfoEndpoint().userService(customOAuth2UserService()).and().successHandler(authenticationSuccessHandler());
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

    public CustomOAuth2UserService customOAuth2UserService(){
        return new CustomOAuth2UserService(memberRepository,passwordEncoder());
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("-----------web configure---------");
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil, CustomUserDetailsService userDetailsService){
        return new TokenCheckFilter(userDetailsService,jwtUtil);
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

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }
}
