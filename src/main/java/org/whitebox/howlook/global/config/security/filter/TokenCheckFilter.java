package org.whitebox.howlook.global.config.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.whitebox.howlook.domain.member.service.CustomUserDetailsService;
import org.whitebox.howlook.global.config.security.exception.AccessTokenException;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {  //토큰 검증 후 정보 contextHolder에 등록
    private final CustomUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
//    @Value("#{${AUTH_WHITELIST_PATH}.split(',')}")
    private String[] whiteList = {"/account","/swagger","/v3/api-docs","/api/v2"};
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        for(String str : whiteList){
            if(path.matches(str+".*")) {
                log.info("pass token filter .....");
                filterChain.doFilter(request, response);
                return;
            }
        }
//        if (Arrays.asList(whiteList). .contains(path)) {
//            log.info("pass token filter .....");
//            filterChain.doFilter(request, response);
//            return;
//        }


        log.info("Token Check Filter.....................");
        log.info("JWTUtil: "+jwtUtil);
        try{
            Map<String,Object> payload = validateAccessToken(request);
            //memberId
            String memberId = (String)payload.get("memberId");
            log.info("memberId: "+memberId);
            UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request,response);
        }catch (AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }
    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {  //토큰검증

        String headerStr = request.getHeader("Authorization");

        if(headerStr == null  || headerStr.length() < 8){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        //Bearer 생략
        String tokenType = headerStr.substring(0,6);
        String tokenStr =  headerStr.substring(7);

        if(tokenType.equalsIgnoreCase("Bearer") == false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);  //토큰 파싱

            return values;
        }catch(MalformedJwtException malformedJwtException){
            log.error("MalformedJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch(SignatureException signatureException){
            log.error("SignatureException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch(ExpiredJwtException expiredJwtException){
            log.error("ExpiredJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }
}
