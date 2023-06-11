package org.whitebox.howlook.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class EditProfileRequest {


    @ApiModelProperty(value = "별명", example = "홍길동", required = true)
    @NotBlank(message = "별명을 입력해주세요")
    @Length(min = 2, max = 12, message = "이름은 2문자 이상 12문자 이하여야 합니다")
    private String memberNickName;

    @ApiModelProperty(value = "키", example = "183", required = true)
    @NotNull(message = "키을 입력해주세요")
    @Range(min = 20,max = 300)
    private Long memberHeight;

    @ApiModelProperty(value = "몸무게", example = "70", required = true)
    @NotNull(message = "몸무게를 입력해주세요")
    @Range(min = 20,max = 300)
    private Long memberWeight;

    @ApiModelProperty(value = "전화번호", example = "01012345678", required = false)
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "휴대폰 번호 양식이 맞지 않습니다")
    private String memberPhone;

}