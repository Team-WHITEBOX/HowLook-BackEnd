package org.whitebox.howlook.domain.member.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberJoinDTO {
    private String mid;
    private String mpw;
    private String name;
    private String nickName;
    private String phone;
    private int height;
    private int weight;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;
    private char gender;
    private String profilePhoto;
    private boolean del;
    private boolean social;
}
