package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.global.config.security.dto.TokenDTO;

public interface OAuth2MemberService {
    TokenDTO loginOauth(String providerName, String code);
}
