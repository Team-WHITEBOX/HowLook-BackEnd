package org.whitebox.howlook.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SocialEditProfileRequest {

//    @ApiModelProperty(value = "아이디", example = "user1", required = true)
//    @NotBlank(message = "아이디을 입력해주세요")
//    @Length(min = 4, max = 12, message = "ID는 4문자 이상 12문자 이하여야 합니다")
//    private String memberId;

    @ApiModelProperty(value = "이름", example = "홍길동", required = true)
    @NotBlank(message = "이름을 입력해주세요")
    @Length(min = 2, max = 12, message = "이름은 2문자 이상 12문자 이하여야 합니다")
    private String memberName;

    @ApiModelProperty(value = "생일", example = "yyyy-MM-dd", required = true)
    private LocalDate memberBirthDay;

    @ApiModelProperty(value = "별명", example = "길동이", required = true)
    @NotBlank(message = "별명을 입력해주세요")
    @Length(min = 2, max = 12, message = "이름은 2문자 이상 12문자 이하여야 합니다")
    private String memberNickName;

    @ApiModelProperty(value = "키", example = "183", required = true)
    @NotNull(message = "키을 입력해주세요")
    private Long memberHeight;

    @ApiModelProperty(value = "몸무게", example = "70", required = true)
    @NotNull(message = "몸무게를 입력해주세요")
    private Long memberWeight;

    @ApiModelProperty(value = "전화번호", example = "01012345678", required = true)
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "휴대폰 번호 양식이 맞지 않습니다")
    private String memberPhone;


}