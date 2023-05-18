package org.whitebox.howlook.domain.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.member.dto.*;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.exception.MemberDoesNotExistException;
import org.whitebox.howlook.domain.member.service.MemberService;
import org.whitebox.howlook.domain.post.dto.SimplePostDTO;
import org.whitebox.howlook.global.config.security.dto.TokenDTO;
import org.whitebox.howlook.global.result.ResultResponse;
import org.whitebox.howlook.global.util.AccountUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AccountUtil accountUtil;

    @ApiOperation(value = "로그아웃")
    @DeleteMapping(value = "/logout")
    public ResponseEntity<ResultResponse> logOut(@RequestBody TokenDTO tokenDTO) {
        memberService.logout(tokenDTO);
        return ResponseEntity.ok(ResultResponse.of(LOGOUT_SUCCESS));
    }

    @ApiOperation(value = "JWT토큰을 이용한 로그인 확인")
    @GetMapping(value = "/check")
    public ResponseEntity<ResultResponse> checkLogin() {
        Member member = accountUtil.getLoginMember();
        if(checkMember(member)) {
            String memberId = member.getMemberId();
            return ResponseEntity.ok(ResultResponse.of(GET_USER_BY_TOKEN_SUCCESS, memberId));
        }
        else {
            throw new MemberDoesNotExistException();
        }
    }

    @ApiOperation(value = "비밀번호 변경")
    @PutMapping(value = "/password")
    public ResponseEntity<ResultResponse> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        log.info("member controller");
        memberService.updatePassword(updatePasswordRequest);

        return ResponseEntity.ok(ResultResponse.of(UPDATE_PASSWORD_SUCCESS));
    }

    @ApiOperation(value = "유저 프로필 조회")
    @GetMapping(value = "/{memberId}")
    public ResponseEntity<ResultResponse> getUserProfile(@PathVariable("memberId") String memberId) {
        final UserProfileResponse userProfileResponse = memberService.getUserProfile(memberId);

        return ResponseEntity.ok(ResultResponse.of(GET_USERPROFILE_SUCCESS, userProfileResponse));
    }

    @ApiOperation(value = "유저 스크랩 게시물 조회")
    @GetMapping("/{memberId}/scrap")
    public ResponseEntity<ResultResponse> getUserScrap(@PathVariable("memberId") String memberId) {
        final ScrapsResponse userScraps = memberService.getUserScrap(memberId);

        return ResponseEntity.ok(ResultResponse.of(GET_MEMBER_SAVED_POSTS_SUCCESS,userScraps));
    }

    @ApiOperation(value = "게시글 작성자 정보 조회")
    @GetMapping(value = "/{memberId}/postinfo")
    public ResponseEntity<ResultResponse> getUserPostInfo(@PathVariable("memberId") String memberId) {
        final UserPostInfoResponse userPostInfoResponse = memberService.getUserPostInfo(memberId);

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
    @ApiOperation(value = "소셜 로그인 회원가입 프로필 수정")
    @PutMapping(value = "/socialedit")
    public ResponseEntity<ResultResponse> socialEditProfile(@Valid @RequestBody SocialEditProfileRequest socialEditProfileRequest) {
        memberService.socialEditProfile(socialEditProfileRequest);
        return ResponseEntity.ok(ResultResponse.of(EDIT_PROFILE_SUCCESS));
    }

    @ApiOperation(value = "회원 프로필 대표 사진 등록")
    @PutMapping(value = "/photo")
    public ResponseEntity<ResultResponse> editProfilePhoto(@RequestParam @NotNull Long postId) {
        memberService.editProfilePhoto(postId);

        return ResponseEntity.ok(ResultResponse.of(GET_EDIT_PROFILE_SUCCESS));
    }

    boolean checkMember(Member member){
        if(!member.isSocial()){
            return true;
        }
        else{
            if(member.getBirthDay()==null || member.getName() == null || member.getHeight()==null || member.getWeight()==null || member.getPhone() == null){
                return false;
            }
            else{
                return true;
            }
        }
    }
}
