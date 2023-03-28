package org.whitebox.howlook.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.chat.dto.ChatRoomDTO;
import org.whitebox.howlook.domain.chat.service.ChatService;
import org.whitebox.howlook.global.result.ResultResponse;

import java.util.ArrayList;
import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.CREATE_POST_SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatService chatService;

    // 채팅 리스트 화면
    @GetMapping("/")
    public ResponseEntity<ResultResponse> chatRoomList(){
        List<ChatRoomDTO> chatRoomDTOS = chatService.chatRoomList();
        return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS, chatRoomDTOS));
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<ResultResponse> createRoom(@RequestParam String name) {
        ChatRoomDTO room = chatService.createRoom(name);
        return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS,room.getRoomId()));
    }
//
//    // 채팅방 입장 화면
//    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
//    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
//    @GetMapping("/room")
//    public ResponseEntity<ResultResponse> roomDetail(Model model, String roomId){
//
//        log.info("roomId {}", roomId);
//        model.addAttribute("room", chatRepository.findRoomById(roomId));
//        return "chatroom";
//    }


    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/userlist")
    public List<String> userList(String roomId) {
        return chatService.userList(roomId);
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