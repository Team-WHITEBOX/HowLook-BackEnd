package org.whitebox.howlook.global.config.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.whitebox.howlook.global.config.security.exception.TokenException;
import org.whitebox.howlook.global.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {  //토큰 검증 후 정보 contextHolder에 등록
    private final JWTUtil jwtUtil;
    private final String[] whiteList;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for(String list : whiteList){
            if(antPathMatcher.match(list,path)){
                log.info("pass token filter .....");
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            // Request Header 에서 JWT 토큰 추출
            String token = resolveToken(request);

            // validateToken 으로 토큰 유효성 검사
            if (jwtUtil.validateToken(token)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }catch (TokenException e){
            e.sendResponseError(response);
        }
    }

    // Request Header 에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        throw new TokenException();
    }
}
