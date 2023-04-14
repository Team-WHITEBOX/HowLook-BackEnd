package org.whitebox.howlook.domain.payment.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class DifferentAmountException extends BusinessException {

    public DifferentAmountException() {
        super(ErrorCode.AMOUNT_NOT_EQUAL);
    }
}
