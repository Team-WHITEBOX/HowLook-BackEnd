package org.whitebox.howlook.domain.payment.service;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.payment.entity.PaymentInfo;
import org.whitebox.howlook.domain.payment.exception.DifferentAmountException;
import org.whitebox.howlook.domain.payment.repository.PaymentRepository;

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

    @Transactional
    public void verifyIamportPayment(IamportResponse<Payment> irsp, int amount) { // 결제가격을 매겨변수로.

        if (irsp.getResponse().getAmount().intValue() != amount) {
            throw new DifferentAmountException();
        }

        PaymentInfo pay = PaymentInfo.builder()
                .payMethod(irsp.getResponse().getPayMethod())
                .impUid(irsp.getResponse().getPayMethod())
                .merchantUid(irsp.getResponse().getMerchantUid())
                .amount(irsp.getResponse().getAmount().intValue())
                .buyerAddr(irsp.getResponse().getBuyerAddr())
                .buyerPostcode(irsp.getResponse().getBuyerPostcode())
                .build();

        payRepository.save(pay);
    }
}
