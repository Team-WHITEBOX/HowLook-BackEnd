package org.whitebox.howlook.domain.payment.dto;

import lombok.*;

@Getter
@Setter
public class TestPayDTO {
    int ruby; // 지불한 금액에 따른 얻은 루비 개수

    int amount; // 지불한 금액
}