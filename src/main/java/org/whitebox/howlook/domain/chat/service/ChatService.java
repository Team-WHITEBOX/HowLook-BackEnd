package org.whitebox.howlook.domain.chat.service;

import org.whitebox.howlook.domain.chat.dto.ChatRoomDTO;

import java.util.ArrayList;
import java.util.List;

public interface ChatService {
    public List<ChatRoomDTO> chatRoomList();
    public ChatRoomDTO createRoom(String roomId);
    public List<String> userList(String roomId);
    public void addUser(String roomId, String userName);
    public void delUser(String roomId, String userName);

}
