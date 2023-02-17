package org.whitebox.howlook.global.config.security.exception;

import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.whitebox.howlook.global.error.ErrorCode.JWT_INVALID;

public class AccessTokenException extends BusinessException {
    public AccessTokenException() {
        super(JWT_INVALID);
    }
    public AccessTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public void sendResponseError(HttpServletResponse response){

        response.setStatus(this.getErrorCode().getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", this.getErrorCode().getMessage(), "time", new Date()));

        try {
            response.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}