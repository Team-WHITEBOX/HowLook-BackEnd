package org.whitebox.howlook.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRegisterDTO {

    @ApiModelProperty(value = "게시글 내용", example = "게시글내용은 없어도 되옵니다.", required = true)
    private String content;         //내용

    private float latitude;         //위도
    private float longitude;        //경도

    @NotNull
    private UploadFileDTO uploadFileDTO;    //upload 도메인에서 불러온 DTO

    //동일 도메인이지만 Hashtag DTO를 불러와 게시글 입력시 같이 사용
    private HashtagDTO hashtagDTO;
}
