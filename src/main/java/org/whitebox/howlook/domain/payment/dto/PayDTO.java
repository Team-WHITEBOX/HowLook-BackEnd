package org.whitebox.howlook.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PayDTO {
    String impUid; // 결제 Uid

    int ruby; // 지불한 금액에 따른 얻은 루비 개수

    int amount; // 지불한 금액
}