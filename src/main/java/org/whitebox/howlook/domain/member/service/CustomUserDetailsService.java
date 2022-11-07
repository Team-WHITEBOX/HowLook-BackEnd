package org.whitebox.howlook.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.global.config.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    //    private final PasswordEncoder passwordEncoder;
//
//    public CustomUserDetailsService(){
//        this.passwordEncoder = new BCryptPasswordEncoder();
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("loadUserByUsername: "+username);
//        UserDetails userDetails = User.builder().username("user1")
//                //.password("1111")
//                .password(passwordEncoder.encode("1111")) //패스워드 인코딩 필요
//                .authorities("ROLE_USER").build();
//        return userDetails;
//    }
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: "+username);
        Optional<Member> result = memberRepository.getWithRoles(username);
        if(result.isEmpty()){  //해당 아이디 유저가 없다면
            throw new UsernameNotFoundException("username not found .... ");
        }
        Member member = result.get();
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(), member.getMpw(), member.getName(),member.getNickName(),member.getPhone(), member.getHeight(),
                member.getWeight(),member.getBirthDay(),member.getGender(),member.getProfilePhotoId(), member.isDel(), false,
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name())).collect(Collectors.toList())
        );
        log.info("memberSecurityDTO");
        log.info(memberSecurityDTO);

        return memberSecurityDTO;
    }
}
