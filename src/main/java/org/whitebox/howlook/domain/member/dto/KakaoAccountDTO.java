package org.whitebox.howlook.domain.member.dto;

import lombok.Data;

//profile_nickname,account_email,gender,birthday
@Data
public class KakaoAccountDTO {
    String email;
    String nickName;
    String birth;
}
