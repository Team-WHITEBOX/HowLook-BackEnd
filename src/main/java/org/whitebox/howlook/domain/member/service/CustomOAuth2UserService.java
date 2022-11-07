package org.whitebox.howlook.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.entity.MemberRole;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.global.config.security.dto.MemberSecurityDTO;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest....");
        log.info(userRequest);

        log.info("oauth2 user.....................................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("NAME: "+clientName);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;
        Map<String,String> account = new HashMap<>();

        switch (clientName){
            case "kakao":
                //email = getKakaoEmail(paramMap);
                account = getKakao(paramMap);
                break;
        }

        log.info("===============================");
        //log.info(email);
        log.info(account);
        log.info("===============================");

        //return oAuth2User;
        return generateDTO(account, paramMap);
    }

    private MemberSecurityDTO generateDTO(Map<String,String> account, Map<String, Object> params){

        Optional<Member> result = memberRepository.findByMid(account.get("email"));

        //데이터베이스에 해당 이메일을 사용자가 없다면
        if(result.isEmpty()){
            //회원 추가 -- mid는 이메일 주소/ 패스워드는 1111
            Member member = Member.builder()
                    .mid(account.get("email"))
                    .mpw(passwordEncoder.encode("1111"))
                    .nickName(account.get("profile"))
                    .gender(account.get("gender").charAt(0))
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            //MemberSecurityDTO 구성 및 반환
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(account.get("email"), "1111",null,account.get("profile"),
                            null,null,null,null,account.get("gender").charAt(0),null,
                            false,true,
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;
        }else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(
                            member.getMid(),
                            member.getMpw(),
                            member.getName(),
                            member.getNickName(),
                            member.getPhone(),
                            member.getHeight(),
                            member.getWeight(),
                            member.getBirthDay(),
                            member.getGender(),
                            member.getProfilePhotoId(),
                            member.isDel(),
                            member.isSocial(),
                            member.getRoleSet()
                                    .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                                    .collect(Collectors.toList())
                    );

            return memberSecurityDTO;
        }
    }


    private String getKakaoEmail(Map<String, Object> paramMap){

        log.info("KAKAO-----------------------------------------");

        Object value = paramMap.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String)accountMap.get("email");

        log.info("email..." + email);

        return email;
    }

    private Map<String,String> getKakao(Map<String, Object> paramMap){

        log.info("KAKAO-----------------------------------------");

        Object value = paramMap.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String)accountMap.get("email");
        Map<String,String> account = new HashMap<>();
        account.put("email",(String) accountMap.get("email"));
        account.put("nickName",(String) accountMap.get("profile"));
        account.put("gender",(String) accountMap.get("gender"));
        account.put("birthDay",(String) accountMap.get("birthday"));
        log.info("account..." + account);

        return account;
    }

}
