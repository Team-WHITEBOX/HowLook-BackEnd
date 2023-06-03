package org.whitebox.howlook.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.whitebox.howlook.domain.member.entity.Member;

@Data
@Builder
@AllArgsConstructor
public class PaymentsDTO {
    String impUid; // impUid

    Member member; // 결제한 유저정보

    int amount; // 지불한 금액

}