package org.whitebox.howlook.domain.payment.controller;

import com.siot.IamportRestClient.Iamport;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.IamportPaycoClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.whitebox.howlook.domain.payment.entity.PaymentInfo;
import org.whitebox.howlook.domain.payment.service.Payservice;

import java.io.IOException;
import java.util.Map;

@RestController
@Log4j2
public class PaymentsController {
    private String imp_key = "7018025722033557";
    private String imp_secret = "s42iOrZu5frpsnEbUw0wK8mFnN7h2j71Awoau7aJ7iK8GSBS7N2ZcbCaY3mucHW2Y1Azny4MzUwY2ukY";

    private final IamportClient iamportClient;

    public PaymentsController() {
        this.iamportClient = new IamportPaycoClient(imp_key, imp_secret);
    }

    @Autowired
    private Payservice payservice;

    // Uid로 결제 정보 찾기
    public IamportResponse<Payment> paymentLookup(String impUid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(impUid);
    }

    // paymentsNo로 결제 정보 찾기
    public IamportResponse<Payment> paymentLookup(long paymentsNo) throws IamportResponseException, IOException {
        PaymentInfo paymentInfo = payservice.paymentLookupService(paymentsNo);
        return iamportClient.paymentByImpUid(paymentInfo.getImpUid());
    }

    @PostMapping("verifyIamport")
    public IamportResponse<Payment> verifyIamport(@RequestBody Map<String,String> map) throws IamportResponseException, IOException {

        String impUid = map.get("imp_uid");
        int amount = Integer.parseInt(map.get("amount"));

        IamportResponse<Payment> irsp = paymentLookup(impUid);

        payservice.verifyIamportPayment(irsp, amount);
        return irsp;
    }
}
