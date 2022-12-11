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
    private String mid;
    private String mpw;
    private String name;
    private String nickName;
    private String phone;
    private int height;
    private int weight;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;
    @ApiModelProperty(value = "성별", example = "M", required = true)
    @NotBlank(message = "새비밀번호를 입력해주세요")
    @Pattern(regexp = "^[MF]{1,1}", message = "성별은 M 또는 F로 입력해주세요")
    private char gender;
    private String profilePhoto;
    private boolean del;
    private boolean social;
}
