package org.whitebox.howlook.global.config.security.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

import static org.whitebox.howlook.global.error.ErrorCode.JWT_INVALID;

public class AccessTokenException extends BusinessException {
    public AccessTokenException() {
        super(JWT_INVALID);
    }
    public AccessTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}