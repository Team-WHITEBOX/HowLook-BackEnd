package org.whitebox.howlook.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class loginDTO {
    @ApiModelProperty(value = "아이디", example = "testuser1", required = true)
    private String memberId;

    @ApiModelProperty(value = "비밀번호", example = "a1234567", required = true)
    private String memberPassword;
}
