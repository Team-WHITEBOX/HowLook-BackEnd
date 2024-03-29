package org.whitebox.howlook.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;
import org.whitebox.howlook.domain.member.service.MemberServiceImpl;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//mockito 단위테스트
@ExtendWith(MockitoExtension.class)
@Slf4j
class MemberAccountControllerTests {

    @InjectMocks
    private MemberAccountController memberAccountController;
    @Mock
    private MemberServiceImpl memberService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberAccountController).build();
    }


    @DisplayName("회원가입 성공")
    @Test
    void joinPOST() throws Exception {
        //given
        MemberJoinDTO memberJoinDTO = joinDTO();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/account/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(memberJoinDTO)));

        // then
        verify(memberService).join(any(MemberJoinDTO.class));
        resultActions.andExpect(status().isCreated());
    }


    private MemberJoinDTO joinDTO() {
        return MemberJoinDTO.builder()
                .memberId("testcode999").memberPassword("a1234567").name("테스트")
                .nickName("테스트코드999").birthDay(LocalDate.now())
                .phone("01012345678").height(180).weight(70).gender("M")
                .build();
    }
}