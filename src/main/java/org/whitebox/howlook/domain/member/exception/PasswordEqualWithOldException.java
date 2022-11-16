package org.whitebox.howlook.domain.member.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class PasswordEqualWithOldException extends BusinessException {

    public PasswordEqualWithOldException() {
        super(ErrorCode.PASSWORD_EQUAL_WITH_OLD);
    }

}