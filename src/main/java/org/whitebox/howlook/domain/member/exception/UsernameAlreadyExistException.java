package org.whitebox.howlook.domain.member.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class UsernameAlreadyExistException extends BusinessException {
    public UsernameAlreadyExistException() {
        super(ErrorCode.USERNAME_ALREADY_EXIST);
    }

}