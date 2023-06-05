package org.whitebox.howlook.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.whitebox.howlook.domain.member.entity.Member;

@Data
@Builder
@AllArgsConstructor
public class PaymentsDTO {
    String impUid; // 결제 Uid

    int amount; // 지불한 금액
}