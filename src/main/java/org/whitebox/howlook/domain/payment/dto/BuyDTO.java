package org.whitebox.howlook.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BuyDTO {
    int ruby; // 루비
}
