package org.whitebox.howlook.domain.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.whitebox.howlook.domain.member.entity.Member;

@ApiModel("유저 정보 수정 응답 모델")
@Getter
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

    public EditProfileResponse(Member member) {
        this.memberId = member.getMemberId();
        this.memberNickName = member.getNickName();
        this.memberHeight = member.getHeight();
        this.memberWeight = member.getWeight();
        this.memberPhone = member.getPhone();
    }

}