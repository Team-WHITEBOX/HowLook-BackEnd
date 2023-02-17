package org.whitebox.howlook.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SearchCategoryDTO {

    @NotNull(message = "해시태그는 모두 입력되어야 합니다.")
    private HashtagDTO hashtagDTO;

    @ApiModelProperty(value = "최대키", example = "180", required = true)
    @NotNull(message = "최대키를 입력해 주세요.")
    @Max(300)
    private Long heightHigh;

    @ApiModelProperty(value = "최소키", example = "150", required = true)
    @NotNull(message = "최소키를 입력해 주세요.")
    @Min(20)
    private Long heightLow;

    @ApiModelProperty(value = "최대몸무게", example = "70", required = true)
    @NotNull(message = "최대몸무게를 입력해 주세요.")
    @Max(300)
    private Long weightHigh;

    @ApiModelProperty(value = "최소몸무게", example = "40", required = true)
    @NotNull(message = "최소몸무게를 입력해 주세요.")
    @Min(20)
    private Long weightLow;

    @NotBlank
    @Pattern(regexp = "[MFA]", message = "성별은 M 또는 F로 입력해주세요")
    private char gender;

    private int page;
    private int size;
}
