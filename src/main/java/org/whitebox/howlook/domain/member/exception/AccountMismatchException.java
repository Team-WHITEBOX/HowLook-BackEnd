package org.whitebox.howlook.domain.member.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class AccountMismatchException extends BusinessException {
    public AccountMismatchException() {
        super(ErrorCode.ACCOUNT_MISMATCH);
    }

}