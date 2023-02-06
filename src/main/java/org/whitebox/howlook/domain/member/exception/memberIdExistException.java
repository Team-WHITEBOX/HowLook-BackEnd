package org.whitebox.howlook.domain.member.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class memberIdExistException extends BusinessException {
    public memberIdExistException() {
        super(ErrorCode.MEMBERID_ALREADY_EXIST);
    }
}
