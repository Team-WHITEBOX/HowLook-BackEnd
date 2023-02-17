package org.whitebox.howlook.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.whitebox.howlook.domain.post.dto.SimplePostDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel("유저 프로필 조회 응답 모델")
@Getter
public class UserProfileResponse {

    @ApiModelProperty(value = "아이디", example = "user1")
    private String memberId;

    @ApiModelProperty(value = "닉네임", example = "길동이")
    private String memberNickName;

    @ApiModelProperty(value = "키", example = "180")
    private Long memberHeight;

    @ApiModelProperty(value = "몸무게", example = "60")
    private Long memberWeight;

    @ApiModelProperty(value = "프로필사진")
    private String profilePhoto;

    @ApiModelProperty(value = "게시글 개수")
    private Long memberPostCount;
    @ApiModelProperty(value = "게시글 목록", example = "['사진1','사진2','사진3']")
    private List<SimplePostDTO> memberPosts;

    @ApiModelProperty(value = "본인 여부", example = "false")
    private boolean isMe;

    @QueryProjection
    public UserProfileResponse(String memberId, String memberNickName, Long memberHeight, Long memberWeight, String profilePhoto,
                               boolean isMe) {
        this.memberId = memberId;
        this.memberNickName = memberNickName;
        this.memberHeight = memberHeight;
        this.memberWeight = memberWeight;
        this.profilePhoto = profilePhoto;
        this.isMe = isMe;
    }
    public void setMemberPosts(List<SimplePostDTO> posts){
        this.memberPosts = posts.stream().collect(Collectors.toList());
        this.memberPostCount = posts.stream().count();
    }

}