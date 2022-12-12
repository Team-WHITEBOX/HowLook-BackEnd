package org.whitebox.howlook.domain.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;
import org.whitebox.howlook.domain.member.dto.loginDTO;
import org.whitebox.howlook.domain.member.service.MemberService;
import org.whitebox.howlook.global.result.ResultResponse;


import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/account")
@Log4j2
@RequiredArgsConstructor
public class MemberAccountController {
    private final MemberService memberService;

    @ApiOperation(value = "로그인")
    @PostMapping("/generateToken")
    public void loginPost(@RequestBody loginDTO loginDTO){
        log.info("로그인");
    }

    @ApiOperation(value = "refresh토큰")
    @PostMapping("/refreshToken")
    public void refreshToken(@RequestBody loginDTO loginDTO){
        log.info("토큰 재발급");
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<ResultResponse> joinPOST(@Valid @RequestBody MemberJoinDTO memberJoinDTO){
        log.info("join post...");
        log.info(memberJoinDTO);

        memberService.join(memberJoinDTO);
        return ResponseEntity.ok(ResultResponse.of(REGISTER_SUCCESS, true));
    }

    @ApiOperation(value = "MemberId 중복 조회")
    @GetMapping(value = "/idcheck")
    public ResponseEntity<ResultResponse> checkMemberId(
            @RequestParam
            @Length(min = 4, max = 12, message = "사용자 이름은 4문자 이상 12문자 이하여야 합니다")
            @Pattern(regexp = "^[0-9a-zA-Z]+$", message = "MemberId엔 대소문자, 숫자만 사용할 수 있습니다.")
            String memberId) {
        final boolean check = memberService.checkMemberId(memberId);
        if (check) {
            return ResponseEntity.ok(ResultResponse.of(CHECK_MEMBERID_GOOD, true));
        } else {
            return ResponseEntity.ok(ResultResponse.of(CHECK_MEMBERID_BAD, false));
        }
    }

    @ApiOperation(value = "nickname 중복 조회")
    @GetMapping(value = "/nickcheck")
    public ResponseEntity<ResultResponse> checkMemberNick(@RequestParam String nickName) {
        final boolean check = memberService.checkNickName(nickName);
        if (check) {
            return ResponseEntity.ok(ResultResponse.of(CHECK_NICKNAME_GOOD, true));
        } else {
            return ResponseEntity.ok(ResultResponse.of(CHECK_NICKNAME_BAD, false));
        }
    }

}
