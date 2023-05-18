package org.whitebox.howlook.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.whitebox.howlook.domain.member.dto.KakaoTokenResponse;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.entity.MemberRole;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.member.dto.OAuth2MemberDTO;
import org.whitebox.howlook.global.config.security.dto.TokenDTO;
import org.whitebox.howlook.global.util.JWTUtil;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2MemberServiceImpl implements OAuth2MemberService {

    private final InMemoryClientRegistrationRepository inMemoryClient;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RedisTemplate redisTemplate;

    @Override
    public TokenDTO loginOauth(String providerName, String code) {
        ClientRegistration provider = inMemoryClient.findByRegistrationId(providerName);
        log.info(provider);
        log.info(providerName);
        KakaoTokenResponse oAuth2Token = getOAuthToken(code, provider);
        log.info(oAuth2Token);
        OAuth2MemberDTO oAuthUser = loginOAuthUser(providerName,provider,oAuth2Token);
        log.info(oAuthUser);

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

    private OAuth2MemberDTO loginOAuthUser(String providerName, ClientRegistration provider, KakaoTokenResponse oAuth2Token) {

        Map<String, Object> paramMap = getUserAttribute(provider,oAuth2Token);
        Map<String,String> account = new HashMap<>();

        switch (providerName){
            case "kakao":
                account = getKakao(paramMap);
                break;
        }
        return generateMember(account);
    }

    private Map<String,Object> getUserAttribute(ClientRegistration provider, KakaoTokenResponse oauth2Token){
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header-> header.setBearerAuth(oauth2Token.getAccessToken().toString()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();
    }

    private Map<String,String> getKakao(Map<String, Object> paramMap){

        Object value = paramMap.get("kakao_account");

        LinkedHashMap accountMap = (LinkedHashMap) value;

        Map<String,String> account = new HashMap<>();
        account.put("email",(String) accountMap.get("email"));
        account.put("nickName",(String) ((Map<String, Object>)accountMap.get("profile")).get("nickname"));
        account.put("gender",(String) accountMap.get("gender"));
        account.put("birthDay",(String) accountMap.get("birthday"));

        return account;
    }

    private OAuth2MemberDTO generateMember(Map<String, String> account) {
        Optional<Member> member = memberRepository.findByMemberId(account.get("email"));

        if (member.isEmpty()) {
            return createNewMember(account);
        } else {
            Member existingMember = member.get();
            return createMemberSecurityDTO(existingMember);
        }
    }

    private OAuth2MemberDTO createNewMember(Map<String, String> account) {
        Member newMember = Member.builder()
                .memberId(account.get("email"))
                .memberPassword(passwordEncoder.encode("1111"))
                .nickName(account.get("nickName"))
                .gender(Character.toUpperCase(account.get("gender").charAt(0)))
                .social(true)
                .build();
        newMember.addRole(MemberRole.USER);
        memberRepository.save(newMember);

        return createMemberSecurityDTO(newMember);
    }

    private OAuth2MemberDTO createMemberSecurityDTO(Member member) {
        return OAuth2MemberDTO.builder().memberId(member.getMemberId())
                .nickName(member.getNickName())
                .name(member.getName())
                .birthDay(member.getBirthDay())
                .gender(member.getGender())
                .social(member.isSocial())
                .roleSet(member.getRoleSet())
                .build();
    }
}
