package org.whitebox.howlook.domain.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.member.dto.*;
import org.whitebox.howlook.domain.member.service.MemberService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "비밀번호 변경")
    @PutMapping(value = "/password")
    public ResponseEntity<ResultResponse> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        log.info("member controller");
        memberService.updatePassword(updatePasswordRequest);

        return ResponseEntity.ok(ResultResponse.of(UPDATE_PASSWORD_SUCCESS));
    }

    @ApiOperation(value = "유저 프로필 조회")
    @GetMapping(value = "/{usermid}")
    public ResponseEntity<ResultResponse> getUserProfile(@PathVariable("usermid") String usermid) {
        final UserProfileResponse userProfileResponse = memberService.getUserProfile(usermid);

        return ResponseEntity.ok(ResultResponse.of(GET_USERPROFILE_SUCCESS, userProfileResponse));
    }

    @ApiOperation(value = "게시글 작성자 정보 조회")
    @GetMapping(value = "/{usermid}/postinfo")
    public ResponseEntity<ResultResponse> getUserPostInfo(@PathVariable("usermid") String usermid) {
        final UserPostInfoResponse userPostInfoResponse = memberService.getUserPostInfo(usermid);

        return ResponseEntity.ok(ResultResponse.of(GET_USERPROFILE_SUCCESS,userPostInfoResponse));
    }

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
}
