package org.whitebox.howlook.domain.member.exception;


import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class MemberDoesNotExistException extends BusinessException {
    public MemberDoesNotExistException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }

}