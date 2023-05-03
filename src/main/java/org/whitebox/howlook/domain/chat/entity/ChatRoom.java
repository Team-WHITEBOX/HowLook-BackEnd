package org.whitebox.howlook.domain.chat.entity;

import lombok.*;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//    @OneToMany(mappedBy = "chatRoom")
//    @Builder.Default
//    private List<ChatRoomMember> memberList = new ArrayList<>();

    public void upUserCount(){
        this.userCount++;
    }
    public void downUserCount(){
        this.userCount--;
    }
}
