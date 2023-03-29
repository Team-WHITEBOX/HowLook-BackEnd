package org.whitebox.howlook.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.chat.dto.ChatRoomDTO;
import org.whitebox.howlook.domain.chat.entity.ChatRoom;
import org.whitebox.howlook.domain.chat.repository.ChatRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.global.config.RootConfig;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final RootConfig rootConfig;
    @Override
    public List<ChatRoomDTO> chatRoomList() {
        List<ChatRoom> chatRoomList = chatRepository.findAll();
        List<ChatRoomDTO> chatRoomDTOS = chatRoomList.stream().map(chatRoom -> new ChatRoomDTO(chatRoom)).collect(Collectors.toList());
        return chatRoomDTOS;
    }

    @Override
    @Transactional
    public ChatRoomDTO createRoom(String roomName) {
        ChatRoom chatRoom = ChatRoom.builder().roomId(UUID.randomUUID().toString()).roomName(roomName).userCount(0).build();
        chatRepository.save(chatRoom);
        return rootConfig.getMapper().map(chatRoom,ChatRoomDTO.class);
    }

    @Override
    public List<String> userList(String roomId) {
        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(CHATROOM_NOT_FOUND));
        List<String> users = chatRoom.getUserList().stream().map(user -> user.getMemberId()).collect(Collectors.toList());
        return users;
    }

    @Override
    @Transactional
    public void addUser(String roomId, String userName) {
        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(CHATROOM_NOT_FOUND));
        Member user = memberRepository.findByMemberId(userName).orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
        chatRoom.upUserCount();
        chatRoom.addUser(user);
    }

    @Override
    @Transactional
    public void delUser(String roomId, String userName) {
        ChatRoom chatRoom = chatRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(CHATROOM_NOT_FOUND));
        Member user = memberRepository.findByMemberId(userName).orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
        chatRoom.downUserCount();
        chatRoom.removeUser(user);
    }
}
