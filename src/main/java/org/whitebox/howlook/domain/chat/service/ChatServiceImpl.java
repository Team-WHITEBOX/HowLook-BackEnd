package org.whitebox.howlook.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.chat.dto.ChatDTO;
import org.whitebox.howlook.domain.chat.dto.ChatRoomDTO;
import org.whitebox.howlook.domain.chat.entity.Chat;
import org.whitebox.howlook.domain.chat.entity.ChatRoom;
import org.whitebox.howlook.domain.chat.entity.ChatRoomMember;
import org.whitebox.howlook.domain.chat.repository.ChatRoomMemberRepository;
import org.whitebox.howlook.domain.chat.repository.ChatRoomRepository;
import org.whitebox.howlook.domain.chat.repository.mongo.ChatRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.global.config.RootConfig;
import org.whitebox.howlook.global.error.exception.BusinessException;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final RootConfig rootConfig;
    private final AccountUtil accountUtil;
    @Override
    public List<ChatRoomDTO> chatRoomList() {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        List<ChatRoomDTO> chatRoomDTOS = chatRoomList.stream().map(chatRoom -> new ChatRoomDTO(chatRoom)).collect(Collectors.toList());
        return chatRoomDTOS;
    }

    @Override
    @Transactional
    public ChatRoomDTO createRoom(String roomName) {
        ChatRoom chatRoom = ChatRoom.builder().roomId(UUID.randomUUID().toString()).roomName(roomName).userCount(0).build();
        chatRoomRepository.save(chatRoom);
        return rootConfig.getMapper().map(chatRoom,ChatRoomDTO.class);
    }

    @Override
    public List<String> roomMemberList(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(CHATROOM_NOT_FOUND));
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByChatRoom(chatRoom);
        List<String> members = new ArrayList<>();
        if(!chatRoomMembers.isEmpty()) {
            members = chatRoomMembers.stream().map(member -> member.getMember().getMemberId()).collect(Collectors.toList());
        }
        return members;
    }

    @Override
    @Transactional
    public void enterRoom(String roomId,String memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(CHATROOM_NOT_FOUND));
        Member member = memberRepository.findByMemberId(memberId).orElseThrow();
        //Member member = accountUtil.getLoginMember();
        chatRoom.upUserCount();
        if(chatRoomMemberRepository.existsByChatRoomAndMember(chatRoom,member)){
            throw new BusinessException(ALREADY_ENTER_ROOM);
        }
        ChatRoomMember chatRoomMember = new ChatRoomMember(chatRoom,member, LocalDateTime.now());
        chatRoomMemberRepository.save(chatRoomMember);
    }

    @Override
    @Transactional
    public void leaveRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(CHATROOM_NOT_FOUND));
        Member member = accountUtil.getLoginMember();
        chatRoom.downUserCount();
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom,member).orElseThrow(()->new EntityNotFoundException(CHATROOM_MEMBER_NOT_FOUND));
        chatRoomMemberRepository.delete(chatRoomMember);
    }

    @Override
    public List<ChatDTO> getChatList(String roomId) {
        Member member = accountUtil.getLoginMember();
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(CHATROOM_NOT_FOUND));
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom,member).orElseThrow(()->new EntityNotFoundException(CHATROOM_MEMBER_NOT_FOUND));
        List<Chat> chats = chatRepository.findAllByRoomIdAndTimeAfter(roomId,chatRoomMember.getEnterTime());
        List<ChatDTO> chatDTOS = chats.stream().map(chat -> rootConfig.getMapper().map(chat, ChatDTO.class)).collect(Collectors.toList());
        return chatDTOS;
    }
}
