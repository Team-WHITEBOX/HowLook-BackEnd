package org.whitebox.howlook.global.config.security.handler;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.global.config.security.dto.TokenDTO;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {  //로그인 성공 토큰반환

    private final JWTUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("Login Success Handler......................");

        String accessToken = jwtUtil.generateToken(authentication.getName(),authentication.getAuthorities());
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());
        redisTemplate.opsForValue().set("RT:"+authentication.getName(),refreshToken, Duration.ofDays(15));

        TokenDTO tokenDTO = TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        Gson gson = new Gson();

        String jsonStr = gson.toJson(tokenDTO);

        response.getWriter().println(jsonStr);

    }
}