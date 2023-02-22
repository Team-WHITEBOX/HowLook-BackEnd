package org.whitebox.howlook.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PostRegisterDTO {

    @ApiModelProperty(value = "게시글 내용", example = "게시글내용은 없어도 되옵니다.")
    private String content;         //내용

    private float latitude;         //위도
    private float longitude;        //경도

    @NotNull
    private UploadFileDTO uploadFileDTO;    //upload 도메인에서 불러온 DTO

    //동일 도메인이지만 Hashtag DTO를 불러와 게시글 입력시 같이 사용
    private HashtagDTO hashtagDTO;
}
