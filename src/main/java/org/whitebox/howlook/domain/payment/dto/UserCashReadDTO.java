package org.whitebox.howlook.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.payment.entity.UserCash;

@Getter
@Setter
public class UserCashReadDTO {
    int ruby;

    public UserCashReadDTO() {
        ruby = 0;
    }
    public UserCashReadDTO(UserCash userCash) {
        this.ruby = userCash.getRuby();
    }
}
