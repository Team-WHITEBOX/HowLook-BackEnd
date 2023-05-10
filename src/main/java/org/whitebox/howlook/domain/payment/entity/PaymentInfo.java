package org.whitebox.howlook.domain.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Payment_Info")
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentNo; // 결제한번호

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member; // 유저 정보

    int amount; // 결제 금액
    
    int ruby; // 결제된 금액에 따른 얻은 루비수

    String impUid; // Uid
}
