package org.whitebox.howlook.global.config.security.handler;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.global.config.security.dto.MemberSecurityDTO;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("----------------------------------------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess ..........");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> claim = Map.of("memberId", memberSecurityDTO.getMemberId());
        //Access Token 유효기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1);
        //Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claim, 30);

        Gson gson = new Gson();

        Map<String,String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken);

        String jsonStr = gson.toJson(keyMap);
        //response.getWriter().println(jsonStr);
        response.sendRedirect("http://3.34.164.14:8080/account/webview.html?accessToken="+accessToken+"&refreshToken="+refreshToken);

//        RequestDispatcher dispatcher = request.getRequestDispatcher("http://localhost:8080/account/webview.html");
//        request.setAttribute("accessToken",accessToken);
//        request.setAttribute("refreshToken",refreshToken);
//        dispatcher.forward(request, response);


//        //소셜로그인이고 회원의 패스워드가 1111이라면
//        if (memberSecurityDTO.isSocial()
//                && (memberSecurityDTO.getMemberPassword().equals("1111")
//                ||  passwordEncoder.matches("1111", memberSecurityDTO.getMemberPassword())
//        )) {
//            log.info("Should Change Password");
//
//            log.info("Redirect to Member Modify ");
//            response.sendRedirect("/member/modify");
//
//            return;
//        } else {
//
//            response.sendRedirect("/board/list");
//        }
    }
}