package org.whitebox.howlook.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.whitebox.howlook.domain.member.entity.Member;

@ApiModel("게시글 작성자 정보 응답 모델")
@Getter
public class UserPostInfoResponse {
    @ApiModelProperty(value = "아이디", example = "user1")
    private String memberId;

    @ApiModelProperty(value = "닉네임", example = "길동이")
    private String memberNickName;

    @ApiModelProperty(value = "키", example = "180")
    private Long memberHeight;

    @ApiModelProperty(value = "몸무게", example = "60")
    private Long memberWeight;

    @ApiModelProperty(value = "프로필사진")
    private Long profilePhotoId;

    @QueryProjection
    public UserPostInfoResponse(String memberId,String memberNickName,Long memberHeight,Long memberWeight,Long profilePhotoId){
        this.memberId=memberId;
        this.memberNickName=memberNickName;
        this.memberHeight=memberHeight;
        this.memberWeight=memberWeight;
        this.profilePhotoId=profilePhotoId;
    };

    public UserPostInfoResponse(Member member){
        this.memberId=member.getMid();
        this.memberNickName=member.getNickName();
        this.memberHeight=member.getHeight();
        this.memberWeight=member.getWeight();
        this.profilePhotoId=member.getProfilePhotoId();
    }
}
