package org.whitebox.howlook.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.chat.dto.ChatDTO;
import org.whitebox.howlook.domain.chat.dto.ChatRoomDTO;
import org.whitebox.howlook.domain.chat.service.ChatService;
import org.whitebox.howlook.global.result.ResultResponse;

import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatService chatService;

    // 채팅방 목록 조회
    @GetMapping("/")
    public ResponseEntity<ResultResponse> chatRoomList(){
        List<ChatRoomDTO> chatRoomDTOS = chatService.chatRoomList();
        return ResponseEntity.ok(ResultResponse.of(GET_CHATROOMLIST_SUCCESS, chatRoomDTOS));
    }

    // 채팅방 생성
    @PostMapping("/")
    public ResponseEntity<ResultResponse> createRoom(@RequestParam String name) {
        ChatRoomDTO room = chatService.createRoom(name);
        return ResponseEntity.ok(ResultResponse.of(CREATE_CHATROOM_SUCCESS,room.getRoomId()));
    }

    // 채팅방 나가기
    @DeleteMapping("/")
    public ResponseEntity<ResultResponse> leaveRoom(@RequestParam String roomId) {
        chatService.leaveRoom(roomId);
        return ResponseEntity.ok(ResultResponse.of(LEAVE_ROOM_SUCCESS));
    }


    // 채팅방 채팅내용 불러오기 (방 열기)
    @GetMapping("/chat")
    public ResponseEntity<ResultResponse> getChatList(String roomId){
        List<ChatDTO> chats = chatService.getChatList(roomId);
        return ResponseEntity.ok(ResultResponse.of(GET_CHATLIST_SUCCESS,chats));
    }


    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/userlist")
    public ResponseEntity<ResultResponse> userList(String roomId) {
        List<String> userList = chatService.roomMemberList(roomId);
        return ResponseEntity.ok(ResultResponse.of(GET_CHAT_USERLIST_SUCCESS,userList));
    }

    // 채팅에 참여한 유저 닉네임 중복 확인
//    @GetMapping("/duplicateName")
//    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username) {
//
//        // 유저 이름 확인
//        String userName = chatRepository.isDuplicateName(roomId, username);
//        log.info("동작확인 {}", userName);
//
//        return userName;
//    }
}