package org.whitebox.howlook.domain.chat.service;

import org.whitebox.howlook.domain.chat.dto.ChatDTO;
import org.whitebox.howlook.domain.chat.dto.ChatRoomDTO;

import java.util.List;

public interface ChatService {
    public List<ChatRoomDTO> chatRoomList();
    public ChatRoomDTO createRoom(String roomId);
    public List<String> roomMemberList(String roomId);
    public void enterRoom(String roomId,String memberId);
    public void leaveRoom(String roomId);
    List<ChatDTO> getChatList(String roomId);
}
