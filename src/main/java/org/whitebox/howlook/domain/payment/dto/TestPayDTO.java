package org.whitebox.howlook.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestPayDTO {
    int ruby; // 지불한 금액에 따른 얻은 루비 개수

    int amount; // 지불한 금액
}