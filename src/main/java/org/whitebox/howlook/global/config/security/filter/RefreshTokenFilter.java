package org.whitebox.howlook.global.config.security.filter;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import org.whitebox.howlook.global.error.exception.BusinessException;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static org.whitebox.howlook.global.error.ErrorCode.REFRESH_FAIL;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final String refreshPath;
    private final JWTUtil jwtUtil;

    // TODO: 2023-05-03 refreshToken 리팩토링 필요
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(!path.equals(refreshPath)){
            log.info("skip refresh token filter .......");
            filterChain.doFilter(request,response);
            return;
        }
        log.info("Refresh Token Filter...run..................1");

        //전송된 json에서 accessToken과 refreshToken을 얻어온다
        Map<String,String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        Authentication authentication = null;

        if (accessToken==null) return;

        try {
            jwtUtil.validateToken(accessToken);
        }catch (ExpiredJwtException e){
            authentication = jwtUtil.getAuthentication(accessToken);
        }catch (Exception e){
            throw new BusinessException(REFRESH_FAIL);
        }

        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
            authentication = jwtUtil.getAuthentication(accessToken);
        }

        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            Map<String, Object> refreshClaims = jwtUtil.parseClaims(refreshToken);
            //Refresh Token의 유효시간이 얼마 남지 않은 경우
            Integer exp = (Integer)refreshClaims.get("exp");

            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);

            Date current = new Date(System.currentTimeMillis());

            //만료 시간과 현재 시간의 간격 계산
            //만일 3일 미만인 경우에는 Refresh Token도 다시 생성
            long gapTime = (expTime.getTime() - current.getTime());

            //이상태까지 오면 무조건 AccessToken은 새로 생성
            accessToken = jwtUtil.generateToken(authentication);

            //RefrshToken이 3일도 안남았다면..
            if(gapTime < (1000 * 60 * 60 * 24 * 3  ) ){
                log.info("new Refresh Token required...  ");
                refreshToken = jwtUtil.generateRefreshToken(authentication.getName());
            }

            sendTokens(accessToken, refreshToken, response);
        }
    }

    private Map<String,String> parseRequestJSON(HttpServletRequest request) {

        //JSON 데이터를 분석해서 memberId, memberPassword 전달 값을 Map으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);

        }catch(Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue,
                "refreshToken", refreshTokenValue));

        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
