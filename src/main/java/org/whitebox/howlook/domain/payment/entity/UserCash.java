package org.whitebox.howlook.domain.payment.entity;

import lombok.*;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.payment.dto.PayDTO;
import org.whitebox.howlook.domain.payment.dto.TestPayDTO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class UserCash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userCashId;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;

    int ruby; // 갖고있는 루비

    public void payRuby(int ruby) { // 루비 지불
        this.ruby -= ruby;
    }

    public void buyRuby(int ruby) { // 루비 구입
        this.ruby += ruby;
    }

    public UserCash(PayDTO payDTO, Member member) {
        this.member = member;
        ruby = payDTO.getRuby();
    }

    public UserCash(TestPayDTO testPayDTO, Member member) {
        this.member = member;
        ruby = testPayDTO.getRuby();
    }
}
