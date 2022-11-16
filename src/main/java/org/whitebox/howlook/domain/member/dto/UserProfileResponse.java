package org.whitebox.howlook.domain.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@ApiModel("유저 프로필 조회 응답 모델")
@Getter
public class UserProfileResponse {

    @ApiModelProperty(value = "유저네임", example = "dlwlrma")
    private String memberUsername;

    @ApiModelProperty(value = "이름", example = "이지금")
    private String memberName;

//    @ApiModelProperty(value = "프로필사진")
//    private Image memberImage;

    @ApiModelProperty(value = "소개", example = "안녕하세요")
    private String memberIntroduce;

    @ApiModelProperty(value = "포스팅 수", example = "90")
    private Long memberPostsCount;

    @ApiModelProperty(value = "본인 여부", example = "false")
    private boolean isMe;

//    @QueryProjection
//    public UserProfileResponse(String username, String name, String website, Image image,
//                               boolean isFollowing, boolean isFollower, boolean isBlocking, boolean isBlocked,
//                               String introduce, Long postsCount, Long followingsCount, Long followersCount,
//                               boolean isMe) {
//        this.memberUsername = username;
//        this.memberName = name;
//        this.memberWebsite = website;
//        this.memberImage = image;
//        this.isFollowing = isFollowing;
//        this.isFollower = isFollower;
//        this.isBlocking = isBlocking;
//        this.isBlocked = isBlocked;
//        this.memberIntroduce = introduce;
//        this.memberPostsCount = postsCount;
//        this.memberFollowingsCount = followingsCount;
//        this.memberFollowersCount = followersCount;
//        this.isMe = isMe;
//        this.hasStory = false;
//        checkBlock();
//    }

}