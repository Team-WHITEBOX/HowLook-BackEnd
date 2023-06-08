package org.whitebox.howlook.domain.payment.exception;

import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

public class LackOfLubyException extends BusinessException {

    public LackOfLubyException() {
        super(ErrorCode.LACK_LUBY);
    }
}
