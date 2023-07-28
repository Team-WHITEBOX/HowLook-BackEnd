# HowLook-BackEnd
Spring Boot+Flutter 를 사용해 개발한 패션 SNS입니다.

시연영상 : https://youtu.be/zA1p0lpGsXU

스웨거 api : 

<br/>

## 개요

> 백엔드 사용기술
- Spring Boot + Java11
- Spring Security + JWT + Oauth2
- Spring Data JPA
- Query DSL
- MySQL
- MongoDB
- Redis
- webSocket + RabbitMQ
- AWS (ec2,s3)
- Swagger
- Rest api


> 시스템 아키텍처

학생들이 진행한 프로젝트로, 대부분 ec2프리티어 내부에 환경을 구성하여 무료로 간단하게 진행했습니다.

![image](https://github.com/Team-WHITEBOX/HowLook-BackEnd/assets/74866067/3b753743-f830-4a1c-a8a9-953efae5cc1f)


>패키지 구조

패키지 구조를 도메인형으로 나누어 직관적으로 구분이 가능하고 협업에 편리하도록 설계하였습니다.

![image](https://github.com/Team-WHITEBOX/HowLook-BackEnd/assets/74866067/3090ef6b-24a1-47bb-a7fb-2674f0c0e133)



>ERD


![image](https://github.com/Team-WHITEBOX/HowLook-BackEnd/assets/74866067/c90d84b5-1421-4d5f-83cf-ad503c88552e)

    
## 주요 구조


- Controller

스프링 서버는 rest api로서 작동하며 통일된 형식으로 응답합니다.
```
@RestController
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AccountUtil accountUtil;
    
    ...
    
    @ApiOperation(value = "회원 프로필 수정 GET")
    @GetMapping(value = "/edit")
    public ResponseEntity<ResultResponse> getMemberEdit() {
        final EditProfileResponse editProfileResponse = memberService.getEditProfile();

        return ResponseEntity.ok(ResultResponse.of(GET_EDIT_PROFILE_SUCCESS, editProfileResponse));
    }

    @ApiOperation(value = "회원 프로필 수정")
    @PutMapping(value = "/edit")
    public ResponseEntity<ResultResponse> editProfile(@Valid @RequestBody EditProfileRequest editProfileRequest) {
        memberService.editProfile(editProfileRequest);

        return ResponseEntity.ok(ResultResponse.of(EDIT_PROFILE_SUCCESS));
    }
    
    ...
    
}
```

- Service

비즈니스 로직을 수행합니다.
```
@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final AccountUtil accountUtil;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    
    ...
    
    @Override
    public EditProfileResponse getEditProfile() {
        final Member member = accountUtil.getLoginMember(); // JWT토큰으로 사용자 추출
        return new EditProfileResponse(member);
    }
    
    @Transactional
    @Override
    public void editProfile(EditProfileRequest editProfileRequest) {
        final Member member = accountUtil.getLoginMember();

        log.info("수정");
        member.updateNickName(editProfileRequest.getMemberNickName());
        member.updateHeight(editProfileRequest.getMemberHeight());
        member.updateWeight(editProfileRequest.getMemberWeight());
        member.updatePhone(editProfileRequest.getMemberPhone());
        memberRepository.save(member);
    }
    
    ...
    
}
```

- Repository

Spring Date JPA가  Repository, 쿼리메서드 기능으로 편리한 간단한 쿼리작업을 쉽게합니다.
```
public interface MemberRepository extends JpaRepository<Member,String>,MemberProfileRepository {
    ...
    
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByMemberId(@Param("memberId") String memberId);
    
    Optional<Member> findByNickName(@Param("nickName") String nickName);
    
    boolean existsByNickName(String nickName);
}    
```
QueryDSL을 사용해 쿼리를 자바코드로 작성하고 개발 효율을 높입니다.
```
@RequiredArgsConstructor
public class MemberProfileRepositoryImpl implements MemberProfileRepository{
    private final JPAQueryFactory queryFactory;
    
    @Override
    public Optional<UserProfileResponse> findUserProfileByMemberIdAndTargetMemberId(String loginMemberId, String memberId){
        return Optional.ofNullable(queryFactory
                .select(new QUserProfileResponse(
                        member.memberId,
                        member.nickName,
                        member.height,
                        member.weight,
                        member.profilePhoto,
                        member.memberId.eq(loginMemberId)))
                .from(member)
                .where(member.memberId.eq(memberId))
                .fetchOne());
    }
    
    ...
}
```

- DTO와 Entity 분리

관심사 분리로 예상치 못한 에러를 방지하고, 필요한 데이터만 전달합니다.

![dto,entity](https://user-images.githubusercontent.com/74866067/229475117-3e72dc18-27ad-4790-844f-8d42dc4b5a2a.JPG)

  
  
## Security

- securityConfig

스프링 시큐리티의 필터를 이용하여 로그인,토큰 발급,인증 과정을 처리하였습니다.

```
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
```
- OAuth2MemberServiceImpl

카카오 로그인 api를 이용하여 소셜로그인을 구현하여 (비회원이라면 회원가입 후) 자체 JWT토큰을 발급합니다.
```
    @Override
    public TokenDTO loginOauth(String providerName, String code) {
        ClientRegistration provider = inMemoryClient.findByRegistrationId(providerName);
        KakaoTokenResponse oAuth2Token = getOAuthToken(code, provider);
        OAuth2MemberDTO oAuthUser = loginOAuthUser(providerName,provider,oAuth2Token);

        String accessToken = jwtUtil.generateToken(oAuthUser.getMemberId(),oAuthUser.getRoleSet());
        String refreshToken = jwtUtil.generateRefreshToken(oAuthUser.getMemberId());
        redisTemplate.opsForValue().set("RT:"+oAuthUser.getMemberId(),refreshToken, Duration.ofDays(15));
        TokenDTO tokenDTO = TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        return tokenDTO;
    }

    private KakaoTokenResponse getOAuthToken(String code, ClientRegistration provider) {
        WebClient webClient = WebClient.builder()
                .baseUrl(provider.getProviderDetails().getTokenUri())
                .defaultHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        KakaoTokenResponse response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", provider.getClientId())
                        .queryParam("redirect_uri", provider.getRedirectUri())
                        .queryParam("code", code)
                        .queryParam("client_secret", provider.getClientSecret())
                        .build())
                .headers(header->header.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();
        if (response != null) {
            return response;
        } else {
            throw new RuntimeException("Failed to retrieve access token from Kakao");
        }
    }
    ..
    ..
```

## 채팅
- ChatController

rabbitMQ를 메시지 브로커로 사용하여 알맞은 목적지에 메시지를 전달하고, 기본적으로 바인딩 시켜둔 queue와 @RabbitListener를 통해 모든 메시지를 MongoDB에 저장합니다.
```
...
    @MessageMapping("chat.message.{chatRoomId}")
    public void sendMessage(@Payload ChatDTO chat, @DestinationVariable String chatRoomId) {
        log.info("CHAT {}", chat);
        chat.setTime(LocalDateTime.now());
        chat.setMessage(chat.getMessage());
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);

    }

    //기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatDTO chatDTO){
        log.info("received : " + chatDTO.getMessage());
        Chat chat = rootConfig.getMapper().map(chatDTO,Chat.class);
        chatRepository.save(chat);
    }
...
```

## 응답 객체
- ResultResponse

RestController의 반환 객체를 통일하였습니다.
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
- 성공 응답 JSON 예시
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

요청 실패 시 에러응답 객체를 통일하였습니다.
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
- 에러 응답 JSON 예시
```
{
  "status": 401,
  "code": "J004",
  "message": "토큰이 없거나 짧습니다.",
  "errors": []
}
```

- GlobalExceptionHandler
```
@RestControllerAdvice
public class GlobalExceptionHandler {
    ...
    
    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode, e.getErrors());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }
    
    ...
}
```

## 컨벤션

>자바 네이밍 컨벤션 준수 (카멜케이스 등)


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


## 기타

> '대경권 SW산학프로젝트 경진대회' 우수상

![20230309_144556](https://user-images.githubusercontent.com/74866067/229505181-9e7c8b67-8853-415a-a21b-61971d3d750a.jpg)

