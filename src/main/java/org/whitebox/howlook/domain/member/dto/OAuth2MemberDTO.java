package org.whitebox.howlook.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.whitebox.howlook.domain.member.entity.MemberRole;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Builder
public class OAuth2MemberDTO {

    private String memberId;
    private String name;
    private String nickName;
    private LocalDate birthDay;
    private char gender;
    private boolean social;
    private Set<MemberRole> roleSet;

    public Collection<? extends GrantedAuthority> getRoleSet() {
        Collection<? extends GrantedAuthority> authorities = roleSet.stream()
                .map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                .collect(Collectors.toList());
        return authorities;
    }
}
