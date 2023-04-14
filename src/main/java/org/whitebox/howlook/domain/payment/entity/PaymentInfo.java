package org.whitebox.howlook.domain.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String payMethod; // 결제방법

    private String impUid; // 아임포트 Uid

    private String merchantUid; // merchant Uid

    private int amount; // 결제 금액

    private String buyerAddr; // 구매자 주소

    private String buyerPostcode; // 구매자 PostCode
}
