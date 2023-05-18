package org.whitebox.howlook.domain.chat.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoom;
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private LocalDateTime enterTime;

    public ChatRoomMember(ChatRoom chatRoom,Member member,LocalDateTime time){
        this.chatRoom = chatRoom;
        this.member = member;
        this.enterTime = time;
    }
}
