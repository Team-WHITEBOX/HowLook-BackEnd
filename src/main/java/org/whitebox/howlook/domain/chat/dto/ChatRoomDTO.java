package org.whitebox.howlook.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.whitebox.howlook.domain.chat.entity.ChatRoom;
import org.whitebox.howlook.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDTO {
    private String roomId; // 채팅방 아이디
    private String roomName; // 채팅방 이름
    private long userCount; // 채팅방 인원수
    private List<String> userList = new ArrayList<>();

    public ChatRoomDTO(ChatRoom chatRoom){
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
        this.userCount = chatRoom.getUserCount();
        this.userList = chatRoom.getUserList().stream().map(userList-> userList.getMemberId()).collect(Collectors.toList());
    }
}