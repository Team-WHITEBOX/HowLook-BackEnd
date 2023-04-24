package org.whitebox.howlook.domain.member.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
@DynamicInsert
public class Member {
    @Id
    private String memberId;

    private String memberPassword;

    private String name;
    private String nickName;
    private String phone;
    private Long height;
    private Long weight;
    private LocalDate birthDay;
    private char gender;
    private String profilePhoto;
    @Column(columnDefinition = "bit(1) default false")
    private boolean del;
    @Column(columnDefinition = "bit(1) default false")
    private boolean social;
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

//    @Builder
//    public Member(String memberId,String memberPassword, String){
//
//    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }
    public void clearRoles(){
        this.roleSet.clear();
    }
}
