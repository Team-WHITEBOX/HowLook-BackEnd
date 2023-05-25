package org.whitebox.howlook.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;
import org.whitebox.howlook.domain.member.service.MemberServiceImpl;
import org.whitebox.howlook.domain.member.service.OAuth2MemberService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//webMvc 슬라이스 테스트
@WebMvcTest(MemberAccountController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
class MemberAccountControllerTests1 {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberServiceImpl memberService;
    @MockBean
    private OAuth2MemberService oAuth2MemberService;



    @DisplayName("회원가입 성공")
    @Test
    //@WithMockUser(roles = "USER")
    void joinPOST() throws Exception {
        //given
        MemberJoinDTO memberJoinDTO = joinDTO();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/account/join")//.with(csrf())
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