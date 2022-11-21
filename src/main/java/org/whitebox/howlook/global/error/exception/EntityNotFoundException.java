package org.whitebox.howlook.global.error.exception;

import org.whitebox.howlook.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}