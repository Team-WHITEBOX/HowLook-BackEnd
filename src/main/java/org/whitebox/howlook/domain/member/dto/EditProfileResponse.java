package org.whitebox.howlook.domain.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.whitebox.howlook.domain.member.entity.Member;

@ApiModel("유저 정보 수정 응답 모델")
@Getter
@Builder
@AllArgsConstructor
public class EditProfileResponse {

    @ApiModelProperty(value = "아이디", example = "user1")
    private String memberId;

    @ApiModelProperty(value = "별명", example = "홍길동")
    private String memberNickName;

    @ApiModelProperty(value = "전화번호", example = "01012345678")
    private String memberPhone;

    @ApiModelProperty(value = "키", example = "183")
    private Long memberHeight;

    @ApiModelProperty(value = "몸무게", example = "70")
    private Long memberWeight;

//    @ApiModelProperty(value = "성별", example = "M")
//    private char memberGender;

//    @ApiModelProperty(value = "프로필사진 URL")
//    private Long profilePhotoId;

    public EditProfileResponse(Member member) {
        this.memberId = member.getMemberId();
        this.memberNickName = member.getNickName();
        this.memberHeight = member.getHeight();
        this.memberWeight = member.getWeight();
//        this.profilePhotoId = member.getProfilePhotoId();
        this.memberPhone = member.getPhone();
//        this.memberGender = member.getGender();
    }

}