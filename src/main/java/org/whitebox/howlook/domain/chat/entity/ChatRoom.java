package org.whitebox.howlook.domain.chat.entity;

import lombok.*;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    private String roomId; // 채팅방 아이디
    private String roomName; // 채팅방 이름
    private long userCount; // 채팅방 인원수
    @OneToMany
    @Builder.Default
    private List<Member> userList = new ArrayList<>();

    public void upUserCount(){
        this.userCount++;
    }
    public void downUserCount(){
        this.userCount--;
    }
    public void addUser(Member userName){
        this.userList.add(userName);
    }
    public void removeUser(Member userName){
        this.userList.remove(userName);
    }
}
