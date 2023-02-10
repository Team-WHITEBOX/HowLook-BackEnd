package org.whitebox.howlook.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberJoinDTO {
    @ApiModelProperty(value = "아이디", example = "testuser1", required = true)
    private String memberId;
    @ApiModelProperty(value = "비밀번호", example = "123456", required = true)
    private String memberPassword;
    @ApiModelProperty(value = "이름", example = "홍길동", required = true)
    private String name;
    @ApiModelProperty(value = "닉네임", example = "길동이", required = true)
    private String nickName;
    @ApiModelProperty(value = "전화번호", example = "01012345678", required = true)
    private String phone;
    @ApiModelProperty(value = "키", example = "180", required = true)
    private int height;
    @ApiModelProperty(value = "몸무게", example = "70", required = true)
    private int weight;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;
    @ApiModelProperty(value = "성별", example = "M", required = true)
    @Pattern(regexp = "^[MF]{1,1}$", message = "성별은 M 또는 F로 입력해주세요")
    private String gender;
    private String profilePhoto;
//    private boolean del;
//    private boolean social;
}
