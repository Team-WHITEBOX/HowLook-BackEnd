package org.whitebox.howlook.global.config.security.handler;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.BusinessException;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {  //로그인 성공 토큰반환

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("Login Success Handler......................");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(authentication);
        String mid = authentication.getName();
        memberRepository.getWithRoles(mid).orElseThrow(()->new BusinessException(ErrorCode.LOGOUT_BY_ANOTHER));
        log.info(mid); //username
        Map<String, Object> claim = Map.of("mid", mid);
        //Access Token 유효기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1);
        //Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claim, 30);

        Gson gson = new Gson();

        Map<String,String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "mid",mid);

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().println(jsonStr);

    }
}