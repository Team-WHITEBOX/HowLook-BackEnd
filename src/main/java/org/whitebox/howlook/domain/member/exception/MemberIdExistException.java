package org.whitebox.howlook.domain.member.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class MemberIdExistException extends BusinessException {
    public MemberIdExistException() {
        super(ErrorCode.MEMBERID_ALREADY_EXIST);
    }
}
