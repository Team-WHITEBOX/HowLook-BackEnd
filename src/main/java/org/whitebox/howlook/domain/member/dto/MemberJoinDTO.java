package org.whitebox.howlook.domain.member.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MemberJoinDTO {
    private String mid;
    private String mpw;
    private String name;
    private String nickName;
    private String phone;
    private int height;
    private int weight;
    private Date birthDay;
    private char gender;
    private Long profilePhotoId;
    private boolean del;
    private boolean social;
}
