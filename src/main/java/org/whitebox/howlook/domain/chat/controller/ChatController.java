package org.whitebox.howlook.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.whitebox.howlook.domain.chat.dto.ChatDTO;
import org.whitebox.howlook.domain.chat.service.ChatService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations template;

    private  final RabbitTemplate rabbitTemplate;
    private final ChatService chatService;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    // MessageMapping 을 통해 webSocket 로 들어오는 메시지를 발신 처리한다.
    // 이때 클라이언트에서는 /pub/chat/message 로 요청하게 되고 이것을 controller 가 받아서 처리한다.
    // 처리가 완료되면 /sub/chat/room/roomId 로 메시지가 전송된다.
    @MessageMapping("chat.enter.{chatRoomId}")
    public void enterUser(@Payload ChatDTO chat, @DestinationVariable String chatRoomId, SimpMessageHeaderAccessor headerAccessor) {

        // 채팅방에 유저 추가
        chatService.addUser(chat.getRoomId(), chat.getSender());

        // socket session 에 유저이름 저장
        headerAccessor.getSessionAttributes().put("userName", chat.getSender());
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setTime(LocalDateTime.now());
        chat.setMessage(chat.getSender() + " 님 입장!!");
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);

    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void sendMessage(@Payload ChatDTO chat, @DestinationVariable String chatRoomId) {
        log.info("CHAT {}", chat);
        chat.setTime(LocalDateTime.now());
        chat.setMessage(chat.getMessage());
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);

    }

    //receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatDTO chat){

        System.out.println("received : " + chat.getMessage());
    }

    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
//    @EventListener
//    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
//        log.info("DisConnEvent {}", event);
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        // stomp 세션에 있던 user닉네임과 roomId를 확인
//        String userName = (String) headerAccessor.getSessionAttributes().get("userName");
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
//
//        log.info("headAccessor {}", headerAccessor);
//
//        // 채팅방 유저 리스트에서 유저 삭제
//        chatService.delUser(roomId, userName);
//
//        if (userName != null) {
//            log.info("User Disconnected : " + userName);
//
//            // builder 어노테이션 활용
//            ChatDTO chat = ChatDTO.builder()
//                    .type(ChatDTO.MessageType.LEAVE)
//                    .sender(userName)
//                    .message(userName + " 님 퇴장!!")
//                    .build();
//
//            template.convertAndSend("/sub/chat/room/" + roomId, chat);
//        }
//    }

}