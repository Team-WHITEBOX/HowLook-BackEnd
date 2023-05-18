package org.whitebox.howlook.global.config.security.filter;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.exception.MemberDoesNotExistException;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.member.service.CustomUserDetailsService;
import org.whitebox.howlook.global.config.security.exception.TokenException;
import org.whitebox.howlook.global.error.exception.BusinessException;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final String refreshPath;
    private final JWTUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(!path.equals(refreshPath)){
            log.info("skip refresh token filter .......");
            filterChain.doFilter(request,response);
            return;
        }
        log.info("Refresh Token Filter...run..................1");
        try {
            //전송된 json에서 accessToken과 refreshToken을 얻어온다
            Map<String,String> tokens = parseRequestJSON(request);

            String accessToken = tokens.get("accessToken");
            String refreshToken = tokens.get("refreshToken");
            HashMap<Object, String> claims = jwtUtil.parseClaimsByExpiredToken(accessToken); //만료된 atk를 검증하고 claim정보를 가져옴
            // atk가 만료되지 않은 상황은 재발급하지 않음
            if(claims == null){
                throw new TokenException(TOKEN_ALIVE);
            }

            jwtUtil.validateToken(refreshToken); //rtk 검증
            Claims refreshClaims = jwtUtil.parseClaims(refreshToken);
            String userName = claims.get("sub");

            String redisToken = (String) redisTemplate.opsForValue().get("RT:"+userName);
            if (!Objects.equals(redisToken, refreshToken)){  //atk의 userName으로 db에 저장된 rtk와 전달받은 rtk를 비교
                throw new TokenException(REFRESH_INVALID);
            }

            Date exp = refreshClaims.getExpiration();
            Date current = Date.from(OffsetDateTime.now().toInstant());

            //만료 시간과 현재 시간의 간격 계산
            //만일 3일 미만인 경우에는 Refresh Token도 다시 생성
            long gapTime = (exp.getTime() - current.getTime());
            if(gapTime < (1000 * 60 * 60 * 24 * 3  ) ){
                log.info("new Refresh Token required...  ");
                refreshToken = jwtUtil.generateRefreshToken(userName);
                redisTemplate.opsForValue().set("RT:"+userName,refreshToken, Duration.ofDays(15));
            }

            Member member = memberRepository.findByMemberId(userName).orElseThrow(()->{throw new MemberDoesNotExistException();});
            Collection<? extends GrantedAuthority> authorities = member.getRoleSet().stream()
                    .map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                    .collect(Collectors.toList());

            accessToken = jwtUtil.generateToken(userName,authorities);

            sendTokens(accessToken, refreshToken, response);
        }catch (TokenException e){
            e.sendResponseError(response);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    private Map<String,String> parseRequestJSON(HttpServletRequest request) {

        //JSON 데이터를 분석해서 토큰 가져옴
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
