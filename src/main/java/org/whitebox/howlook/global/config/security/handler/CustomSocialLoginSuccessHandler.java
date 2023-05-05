package org.whitebox.howlook.global.config.security.handler;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.whitebox.howlook.global.config.security.dto.MemberSecurityDTO;
import org.whitebox.howlook.global.config.security.dto.TokenDTO;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;


@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("----------------------------------------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess ..........");
        log.info(authentication.getPrincipal());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String accessToken = jwtUtil.generateToken(authentication.getName(),authentication.getAuthorities());
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());
        redisTemplate.opsForValue().set("RT:"+authentication.getName(),refreshToken, Duration.ofDays(15));
        TokenDTO tokenDTO = TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();

        Gson gson = new Gson();

//        Map<String,String> keyMap = Map.of(
//                "accessToken", accessToken,
//                "refreshToken", refreshToken);

        String jsonStr = gson.toJson(tokenDTO);
        //response.getWriter().println(jsonStr);
        response.sendRedirect("http://3.34.164.14:8080/account/webview.html?accessToken="+tokenDTO.getAccessToken()+"&refreshToken="+tokenDTO.getRefreshToken());

    }
}