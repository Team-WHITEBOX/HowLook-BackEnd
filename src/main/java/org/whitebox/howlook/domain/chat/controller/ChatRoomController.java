package org.whitebox.howlook.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.domain.chat.dto.ChatRoom;
import org.whitebox.howlook.domain.chat.repository.ChatRepository;
import org.whitebox.howlook.global.result.ResultResponse;

import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.CREATE_POST_SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRepository chatRepository;

    // 채팅 리스트 화면
    @GetMapping("/")
    public ResponseEntity<ResultResponse> goChatRoom(){
        List<ChatRoom> chatRooms = chatRepository.findAllRoom();
        return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS,chatRooms));
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<ResultResponse> createRoom(@RequestParam String name) {
        ChatRoom room = chatRepository.createChatRoom(name);
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

}