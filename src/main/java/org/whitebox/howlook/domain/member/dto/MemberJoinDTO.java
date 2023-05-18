package org.whitebox.howlook.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinDTO {
    @ApiModelProperty(value = "아이디", example = "testuser1", required = true)
    @NotBlank(message = "아이디를 입력해주세요")
    @Length(min = 4, max = 12, message = "아이디는 4문자 이상 12문자 이하여야 합니다")
    @Pattern(regexp = "^[0-9a-zA-Z]+$",message = "아이디는 대소문자,숫자만 사용할 수 있습니다.")
    private String memberId;

    @ApiModelProperty(value = "비밀번호", example = "a1234567", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상, 최소 하나의 문자와 숫자가 필요합니다")
    @Length(min = 8,max = 20, message = "비밀번호는 8문자 이상 20문자 이하여야 합니다")
    private String memberPassword;

    @ApiModelProperty(value = "이름", example = "홍길동", required = true)
    @NotBlank(message = "이름을 입력해주세요")
    @Length(min = 2, max = 12, message = "이름은 2문자 이상 12문자 이하여야 합니다")
    private String name;

    @ApiModelProperty(value = "닉네임", example = "길동이", required = true)
    @NotBlank(message = "닉네임을 입력해주세요")
    @Length(min = 2, max = 12, message = "닉네임은 2문자 이상 12문자 이하여야 합니다")
    private String nickName;

    @ApiModelProperty(value = "전화번호", example = "01012345678", required = true)
    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "전화번호는 - 없이 입력해주세요.")
    private String phone;

    @ApiModelProperty(value = "키", example = "180", required = true)
    @NotNull(message = "키를 입력해주세요")
    @Range(min = 20,max = 300)
    private int height;

    @ApiModelProperty(value = "몸무게", example = "70", required = true)
    @NotNull(message = "몸무게를 입력해주세요")
    @Range(min = 20,max = 300)
    private int weight;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "생일을 입력해주세요")
    private LocalDate birthDay;

    @ApiModelProperty(value = "성별", example = "M", required = true)
    @Pattern(regexp = "^[MF]{1,1}$", message = "성별은 M 또는 F로 입력해주세요")
    private String gender;
}
