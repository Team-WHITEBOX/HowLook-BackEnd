package org.whitebox.howlook.domain.payment.service;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.payment.dto.PaymentsDTO;
import org.whitebox.howlook.domain.payment.entity.PaymentInfo;
import org.whitebox.howlook.domain.payment.exception.DifferentAmountException;
import org.whitebox.howlook.domain.payment.repository.PaymentRepository;
import org.whitebox.howlook.global.error.GlobalExceptionHandler;

@Log4j2
@Service
public class Payservice {
    @Autowired
    private PaymentRepository payRepository;

    @Transactional
    public PaymentInfo paymentLookupService(long paymentsNO) {
        PaymentInfo paymentsInfo = payRepository.getById(paymentsNO);
        return paymentsInfo;
    }

    @Transactional // 프론트에게 쏴줄 정보
    public void verifyIamportPayment(IamportResponse<Payment> irsp, PaymentsDTO paymentsDTO) { // 결제가격을 매겨변수로.

        if (irsp.getResponse().getAmount().intValue() != paymentsDTO.getAmount()) {
            throw new DifferentAmountException();
        }

        int ruby = paymentsDTO.getAmount() / 100; // 루비개수

        PaymentInfo pay = PaymentInfo.builder()
                .impUid(irsp.getResponse().getImpUid())
                .member(paymentsDTO.getMember())
                .amount(paymentsDTO.getAmount())
                .ruby(ruby)
                .build();

        payRepository.save(pay);
    }
}
