# HowLook-BackEnd
Spring Boot+Flutter 를 사용해 개발한 패션 SNS입니다.

<br/>

## 개요

>사용기술
- Spring Boot
- Spring Security + JWT + Oauth2
- JPA
- Query DSL
- Mysql
- AWS

>패키지 구조

![패키지 구조](https://user-images.githubusercontent.com/74866067/229355074-5649c3a0-c9fd-4ec6-996a-1314a74038e6.JPG)



>ERD

![2erd](https://user-images.githubusercontent.com/74866067/229354467-d5980b0b-ff2b-4398-b1f3-24fae953d597.jpg)

  
    
## Security

- securityConfig
```
        http.authenticationManager(authenticationManager);
        http.httpBasic().disable();
        http.formLogin().disable();
        http.csrf().disable();  // csrf 비활성화
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // JWT위해 세션 사용안함
        http.authorizeRequests()
                .antMatchers(WHITELIST).permitAll()
                .antMatchers("/sample/doB").hasAnyRole("ADMIN")
                .antMatchers("/member/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(apiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenCheckFilter(jwtUtil,userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RefreshTokenFilter("/account/refreshToken",jwtUtil), TokenCheckFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()); // 403
        http.cors(httpSecurityCorsConfigurer -> {         //cors문제 해결
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });
        http.oauth2Login().userInfoEndpoint().userService(customOAuth2UserService()).and().successHandler(authenticationSuccessHandler());
```

## 응답 객체
- ResultResponse
```
public class ResultResponse {
    @ApiModelProperty(value = "Http 상태 코드")
    private int status;
    @ApiModelProperty(value = "Business 상태 코드")
    private String code;
    @ApiModelProperty(value = "응답 메세지")
    private String message;
    @ApiModelProperty(value = "응답 데이터")
    private Object data;

    public ResultResponse(ResultCode resultCode, Object data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }
    public static ResultResponse of(ResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }
    public static ResultResponse of(ResultCode resultCode) {
        return new ResultResponse(resultCode, "");
    }
}
```
- 성공 응답 JSON
```
{
  "status": 200,
  "code": "M004",
  "message": "회원 프로필 수정정보를 조회하였습니다.",
  "data": {
    "memberId": "testuser10",
    "memberNickName": "길동이",
    "memberPhone": "01012345678",
    "memberHeight": 180,
    "memberWeight": 70
  }
}
```

- ErrorResponse
```
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private List<FieldError> errors;

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.code = code.getCode();
    }

    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }
    ...
}
```
- 에러 응답 JSON
```
{
  "status": 401,
  "code": "J004",
  "message": "토큰이 없거나 짧습니다.",
  "errors": []
}
```

## 컨벤션

>커밋 컨벤션

```
type: subject

body (optional)
...
...
...

footer (optional)
```

타입	설명
- feat	새로운 기능 추가
- fix	버그 수정
- docs	문서 수정
- style	공백, 세미콜론 등 스타일 수정
- refactor	코드 리팩토링
- perf	성능 개선
- test	테스트 추가
- chore	빌드 과정 또는 보조 기능(문서 생성기능 등) 수정
