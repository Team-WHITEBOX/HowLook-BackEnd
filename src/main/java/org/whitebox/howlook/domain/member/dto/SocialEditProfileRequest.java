package org.whitebox.howlook.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
public class SocialEditProfileRequest {

    @ApiModelProperty(value = "이름", example = "홍길동", required = true)
    @NotBlank(message = "이름을 입력해주세요")
    @Length(min = 2, max = 12, message = "이름은 2문자 이상 12문자 이하여야 합니다")
    private String name;

    @ApiModelProperty(value = "생일", example = "yyyy-MM-dd", required = true)
    @NotNull(message = "생일을 입력해주세요")
    private LocalDate birthDay;

    @ApiModelProperty(value = "별명", example = "길동이", required = true)
    @NotBlank(message = "별명을 입력해주세요")
    @Length(min = 2, max = 12, message = "이름은 2문자 이상 12문자 이하여야 합니다")
    private String nickName;

    @ApiModelProperty(value = "키", example = "183", required = true)
    @NotNull(message = "키을 입력해주세요")
    @Range(min = 20,max = 300)
    private Long height;

    @ApiModelProperty(value = "몸무게", example = "70", required = true)
    @NotNull(message = "몸무게를 입력해주세요")
    @Range(min = 20,max = 300)
    private Long weight;

    @ApiModelProperty(value = "전화번호", example = "01012345678", required = true)
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "휴대폰 번호 양식이 맞지 않습니다")
    private String phone;


}