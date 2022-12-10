package org.whitebox.howlook.domain.member.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
@DynamicInsert
public class Member {
    @Id
    private String mid;         //memberID
    private String mpw;         //memberPW

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
//    public Member(String mid,String mpw, String){
//
//    }

    public void updatePassword(String mpw){ this.mpw = mpw; }
    public void updateNickName(String nickName){
        this.nickName = nickName;
    }
    public void updatePhone(String phone){
        this.phone = phone;
    }
    public void updateHeight(Long height){
        this.height = height;
    }
    public void updateWeight(Long weight){
        this.weight = weight;
    }
    public void updateDel(boolean del){
        this.del = del;
    }
    public void updateSocial(boolean social){
        this.social = social;
    }
    public void updateProfilePhotoId(String profilePhoto){
        this.profilePhoto = profilePhoto;
    }
    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }
    public void clearRoles(){
        this.roleSet.clear();
    }
}
