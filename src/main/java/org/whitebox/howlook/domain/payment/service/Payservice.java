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
import org.whitebox.howlook.global.util.AccountUtil;

@Log4j2
@Service
public class Payservice {
    @Autowired
    private PaymentRepository payRepository;

    private AccountUtil accountUtil;

    @Transactional
    public PaymentInfo paymentLookupService(long paymentsNO) {
        PaymentInfo paymentsInfo = payRepository.getById(paymentsNO);
        return paymentsInfo;
    }

    /* 실전용 */
    @Transactional
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

    /* 테스트용 */
//    @Transactional
//    public void verifyIamportPayment(IamportResponse<Payment> irsp, int amount) {
//        if(irsp.getResponse().getAmount().intValue() != amount) {
//            // 오류 메시지 띄우기
//            log.info("오류");
//            return;
//        }
//
//        int ruby = amount / 100; // 루비개수
//
//        PaymentInfo pay = PaymentInfo.builder()
//                .impUid(irsp.getResponse().getImpUid())
//                .member(null)
//                .amount(amount)
//                .ruby(ruby)
//                .build();
//
//        payRepository.save(pay);
//    }
}
