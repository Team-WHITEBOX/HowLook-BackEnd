package org.whitebox.howlook.global.error.exception;

import org.whitebox.howlook.global.error.ErrorCode;

public class EntityAlreadyExistException extends BusinessException {

    public EntityAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}