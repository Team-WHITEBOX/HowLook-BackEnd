package org.whitebox.howlook.domain.member.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.entity.MemberRole;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.post.repository.PostRepository;
import org.whitebox.howlook.domain.post.repository.ScrapRepository;
import org.whitebox.howlook.global.util.AccountUtil;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MemberServiceTests {
    @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AccountUtil accountUtil;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ScrapRepository scrapRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("회원가입")
    @Test
    void joinTest() {
        // given
        String name = "jointest999";
        MemberJoinDTO memberJoinDTO = joinDTO(name);
        Member member = joinResponse(name);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        //when
        Member result = memberService.join(memberJoinDTO);

        // then
        Assertions.assertThat(result).isEqualTo(member);
    }

    private MemberJoinDTO joinDTO(String name) {
        return MemberJoinDTO.builder()
                .memberId(name).memberPassword("a1234567").name("테스트")
                .nickName(name).birthDay(LocalDate.now())
                .phone("01012345678").height(180).weight(70).gender("M")
                .build();
    }

    private Member joinResponse(String name){
        return Member.builder()
                .memberId(name).memberPassword("a1234567").gender('M').nickName(name)
                .birthDay(LocalDate.now()).del(false).height(180L).weight(70L).name("테스트")
                .phone("01012345678").profilePhoto(null).social(false).roleSet(Collections.singleton(MemberRole.USER))
                .build();
    }
}