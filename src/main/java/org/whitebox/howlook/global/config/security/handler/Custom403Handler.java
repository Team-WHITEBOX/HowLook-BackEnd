package org.whitebox.howlook.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.whitebox.howlook.global.error.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.whitebox.howlook.global.error.ErrorCode.AUTHORITY_INVALID;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("--------Access DENIED-----------");
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        //json 요청 확인
//        String contentType = request.getHeader("Content-Type");
//        boolean jsonRequest = contentType.startsWith("application/json");
//        log.info("isJSON: "+jsonRequest);
//        //일반 request
//        if(!jsonRequest){
//            response.sendRedirect("/member/login?error=ACCESS_DENIED");
//        }
        response.setStatus(AUTHORITY_INVALID.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, ErrorResponse.of(AUTHORITY_INVALID));
            os.flush();
        }
    }
}
