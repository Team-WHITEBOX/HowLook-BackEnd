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
    private long paymentNo;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member; // 유저 정보

    String impUid; // 결제 Uid

    int amount; // 결제 금액
}
