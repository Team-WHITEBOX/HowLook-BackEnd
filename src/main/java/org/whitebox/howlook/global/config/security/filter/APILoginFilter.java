package org.whitebox.howlook.global.config.security.filter;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {   //전달받은 id,pw로 로그인 시도
    public APILoginFilter(String defaultFilterProcessesUrl){
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("APILoginFilter------------------------------------------");
        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("GET METHOD NOT SUPPORT");
            return null;
        }
        Map<String,String> jsonData = parseRequestJSON(request);
        log.info(jsonData);

        if(jsonData == null){
            return null;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                jsonData.get("mid"),
                jsonData.get("mpw")
        );
        return getAuthenticationManager().authenticate(authenticationToken);  //실제 검증 , loadUserByUsername 실행됨
    }

    private Map<String,String> parseRequestJSON(HttpServletRequest request){
        //JSON데이터 분석해서 mid,mpw값을 map으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){
            Gson gson = new Gson();
            return gson.fromJson(reader,Map.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
