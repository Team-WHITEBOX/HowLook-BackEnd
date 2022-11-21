package org.whitebox.howlook.domain.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;
import org.whitebox.howlook.domain.member.service.MemberService;
import org.whitebox.howlook.global.result.ResultResponse;


import static org.whitebox.howlook.global.result.ResultCode.REGISTER_SUCCESS;

@RestController
//@Controller
@RequestMapping("/account")
@Log4j2
@RequiredArgsConstructor
public class MemberAccountController {
    private final MemberService memberService;

    @GetMapping("/login")
    public void loginGET(String error,String logout){
        log.info("login get............");
        log.info("logout: "+logout);
        if (logout != null) {
            log.info("user logout......");
        }
    }

    @GetMapping("/join")
    public void joinGET(){
        log.info("join get....");
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<ResultResponse> joinPOST(@RequestBody MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){
        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        }catch (MemberService.MidExistException e){
            //return ResponseEntity.ok(ResultResponse.of(USERNAME_ALREADY_EXIST, false));
            //redirectAttributes.addFlashAttribute("error","mid");
            //return "redirect:/member/join";
        }
        return ResponseEntity.ok(ResultResponse.of(REGISTER_SUCCESS, true));
        //redirectAttributes.addFlashAttribute("result","success");
        //return "redirect:/member/login";
    }

}
