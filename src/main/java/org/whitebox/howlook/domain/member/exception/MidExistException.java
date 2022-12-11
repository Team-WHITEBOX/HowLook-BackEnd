package org.whitebox.howlook.domain.member.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class MidExistException extends BusinessException {
    public MidExistException() {
        super(ErrorCode.MEMBERID_ALREADY_EXIST);
    }
}
