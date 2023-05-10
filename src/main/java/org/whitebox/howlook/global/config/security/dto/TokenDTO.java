package org.whitebox.howlook.global.config.security.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TokenDTO {
    //private String grantType; // Bearer
    private String accessToken;
    private String refreshToken;
}
