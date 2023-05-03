package org.whitebox.howlook.global.config.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDTO {
    //private String grantType; // Bearer
    private String accessToken;
    private String refreshToken;
}
