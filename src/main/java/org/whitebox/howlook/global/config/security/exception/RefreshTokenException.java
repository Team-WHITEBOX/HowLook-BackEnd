package org.whitebox.howlook.global.config.security.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

import static org.whitebox.howlook.global.error.ErrorCode.REFRESH_INVALID;

public class RefreshTokenException extends BusinessException {
    public RefreshTokenException() {
        super(REFRESH_INVALID);
    }
    public RefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
