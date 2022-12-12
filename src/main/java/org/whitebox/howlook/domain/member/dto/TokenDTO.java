package org.whitebox.howlook.domain.member.dto;

import lombok.Getter;

@Getter
public class TokenDTO {
    String accessToken;
    String refreshToken;
}
