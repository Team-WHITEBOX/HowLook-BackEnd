package org.whitebox.howlook.domain.payment.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.IamportPaycoClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.whitebox.howlook.domain.payment.dto.PayDTO;
import org.whitebox.howlook.domain.payment.dto.TestPayDTO;
import org.whitebox.howlook.domain.payment.dto.UserCashReadDTO;
import org.whitebox.howlook.domain.payment.entity.PaymentInfo;
import org.whitebox.howlook.domain.payment.entity.UserCash;
import org.whitebox.howlook.domain.payment.service.Payservice;
import org.whitebox.howlook.global.result.ResultCode;
import org.whitebox.howlook.global.result.ResultResponse;
import org.whitebox.howlook.global.util.AccountUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;

@RequestMapping("/payment")
@RestController
@Log4j2
@RequiredArgsConstructor
public class PaymentsController {
    @Value("${org.whitebox.payment.key}")
    private String imp_key;

    @Value("${org.whitebox.payment.secret}")
    private String imp_secret;

    @Autowired
    private Payservice payservice;

    @Autowired
    private AccountUtil accountUtil;

    // 결제 Uid로 결제 정보 찾기
    public IamportResponse<Payment> paymentLookup(String impUid) throws IamportResponseException, IOException {
        IamportClient iamportClient = new IamportPaycoClient(imp_key,imp_secret);

        return iamportClient.paymentByImpUid(impUid);
    }

    // paymentsNo로 결제 정보 찾기
    public IamportResponse<Payment> paymentLookup(long paymentsNo) throws IamportResponseException, IOException {
        IamportClient iamportClient = new IamportPaycoClient(imp_key,imp_secret);
        PaymentInfo paymentInfo = payservice.paymentLookupService(paymentsNo);

        return iamportClient.paymentByImpUid(paymentInfo.getImpUid());
    }

    /* 실전 버전 사용자 출금 정보 검증 후 결제 */
    @PostMapping("/charge")
    public ResponseEntity<ResultResponse> chargeRuby(@Valid @ModelAttribute PayDTO payDTO) throws IamportResponseException, IOException {
        String impUid = payDTO.getImpUid();
        IamportResponse<Payment> irsp = paymentLookup(impUid);
        UserCash userCash = payservice.chargeCash(irsp,payDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PAYMENT_SUCCESS, userCash));
    }

    /* 테스트 버전 결제 */
    @PostMapping("/Testcharge") // 테스트용
    public ResponseEntity<ResultResponse> testChargeRuby(@RequestBody TestPayDTO testPayDTO) {
        UserCash userCash = payservice.testChargeCash(testPayDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PAYMENT_SUCCESS, userCash));
    }

    @GetMapping("/readUsercash")
    public ResponseEntity<ResultResponse> readRuby() {
        UserCashReadDTO userCashReadDTO = payservice.getUserCashReadDTO(accountUtil.getLoginMember());
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_USERCASH_SUCCESS,userCashReadDTO));
    }
}