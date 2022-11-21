package org.whitebox.howlook.global.config.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String mid;
    private String mpw;
    private String name;
    private String nickName;
    private String phone;
    private Long height;
    private Long weight;
    private LocalDate birthDay;
    private char gender;
    private Long profilePhotoId;
    private boolean del;
    private boolean social;
    private Map<String,Object> props; //소셜 로그인 정보

    public MemberSecurityDTO(String username, String password,String name, String nickName,
                             String phone,Long height,Long weight, LocalDate birthDay,char gender,Long profilePhotoId,
                             boolean del, boolean social, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mid = username;
        this.mpw = password;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.height = height;
        this.weight = weight;
        this.birthDay = birthDay;
        this.gender = gender;
        this.profilePhotoId = profilePhotoId;
        this.del = del;
        this.social = social;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.mid;
    }
}
