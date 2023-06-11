package org.whitebox.howlook.domain.payment.service;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.payment.dto.BuyDTO;
import org.whitebox.howlook.domain.payment.dto.PayDTO;
import org.whitebox.howlook.domain.payment.dto.TestPayDTO;
import org.whitebox.howlook.domain.payment.entity.PaymentInfo;
import org.whitebox.howlook.domain.payment.entity.UserCash;
import org.whitebox.howlook.domain.payment.exception.DifferentAmountException;
import org.whitebox.howlook.domain.payment.repository.PaymentRepository;
import org.whitebox.howlook.domain.payment.repository.UserCashRepository;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import java.util.Optional;

@Log4j2
@Service
@NoArgsConstructor
public class Payservice {
    @Autowired
    private PaymentRepository payRepository;

    private AccountUtil accountUtil;

    private UserCashRepository userCashRepository;

    @Transactional
    public PaymentInfo paymentLookupService(long paymentsNO) {
        Optional<PaymentInfo> paymentOptional = payRepository.findById(paymentsNO);
        PaymentInfo paymentsInfo = paymentOptional.orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
        return paymentsInfo;
    }

    @Transactional // 캐시 충전
    public UserCash chargeCash(IamportResponse<Payment> irsp, PayDTO paymentsDTO) {
        Member member = accountUtil.getLoginMember();
        if (irsp.getResponse().getAmount().intValue() != paymentsDTO.getAmount()) {
            // 클라이언트 페이지에서 사용자의 출금정보와 아임포트에서의 출금정보를 비교
            throw new DifferentAmountException();
        }

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .impUid(irsp.getResponse().getImpUid())
                .member(member)
                .amount(paymentsDTO.getAmount())
                .ruby(paymentsDTO.getRuby())
                .build();

        payRepository.save(paymentInfo); // 구매내역 저장

        UserCash userCash = userCashRepository.findByMember(member); // 사용자의 캐시 정보 확인

        if (userCash != null) {
            userCash.buyRuby(paymentInfo.getRuby());
            userCashRepository.save(userCash);
        }

        else {
            userCash = new UserCash(paymentsDTO,member);
            userCashRepository.save(userCash);
        }

        return userCash;
    }

    /* 테스트용 */
    @Transactional
    public UserCash testChargeCash(TestPayDTO testPayDTO) {
        Member member = accountUtil.getLoginMember();

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .impUid("12345")
                .member(member)
                .amount(testPayDTO.getAmount())
                .ruby(testPayDTO.getRuby())
                .build();

        payRepository.save(paymentInfo); // 구매내역 저장

        UserCash userCash = userCashRepository.findByMember(member); // 사용자의 캐시 정보 확인

        if (userCash != null) {
            userCash.buyRuby(paymentInfo.getRuby());
            userCashRepository.save(userCash);
        }

        else {
            userCash = new UserCash(testPayDTO,member);
            userCashRepository.save(userCash);
        }

        return userCash;
    }
}
