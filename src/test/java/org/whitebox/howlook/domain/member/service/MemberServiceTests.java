package org.whitebox.howlook.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;
import org.whitebox.howlook.domain.member.repository.MemberRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberServiceTests {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    
    @DisplayName("회원가입")
    @Test
    void joinTest() {
        // given
        MemberJoinDTO memberJoinDTO = joinDTO();

        //when
        memberService.join(joinDTO());

        // then
    }

    private MemberJoinDTO joinDTO() {
        return MemberJoinDTO.builder()
                .memberId("testcode999").memberPassword("a1234567").name("테스트")
                .nickName("테스트코드999").birthDay(LocalDate.now())
                .phone("01012345678").height(180).weight(70).gender("M")
                .build();
    }
}