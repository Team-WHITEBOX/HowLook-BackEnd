package org.whitebox.howlook.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.whitebox.howlook.global.util.JWTUtil;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Test
//    public void testGenerate(){
//        Map<String, Object> claimMap = Map.of("memberId","ABCDE");
//        String jwtStr = jwtUtil.generateToken(claimMap,1);
//        log.info(jwtStr);
//    }
//
//    @Test
//    public void testValidate(){
//        //유효시간 지난 토큰
//        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Njc4OTg0NzcsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNjY3ODk4NDE3fQ.5t3dWHGGiPu7KzhEN64f2DBNZhuQvMinnVYRLdmrGFg";
//
//        Map<String,Object> claim = jwtUtil.validateToken(jwtStr);
//        log.info(claim);
//    }
//
//    @Test
//    public void testAll(){
//        String jwtStr = jwtUtil.generateToken(Map.of("memberId","AAAA","email","aaaa@bbb.com"),1);
//        log.info(jwtStr);
//        Map<String,Object> claim = jwtUtil.validateToken(jwtStr);
//        log.info("memberId: "+claim.get("memberId"));
//        log.info("EMAIL: "+claim.get("email"));
//    }
}
