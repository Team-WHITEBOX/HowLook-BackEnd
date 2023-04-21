package org.whitebox.howlook.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.chat.dto.ChatDTO;
import org.whitebox.howlook.domain.chat.entity.Chat;
import org.whitebox.howlook.domain.chat.repository.mongo.ChatRepository;
import org.whitebox.howlook.domain.chat.service.ChatService;
import org.whitebox.howlook.global.config.RootConfig;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    //private final SimpMessageSendingOperations template;

    private  final RabbitTemplate rabbitTemplate;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final RootConfig rootConfig;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    // /pub/chat.message.{roomId} 로 요청하면 브로커를 통해 처리
    // /exchange/chat.exchange/room.{roomId} 를 구독한 클라이언트에 메시지가 전송된다.
    @MessageMapping("chat.enter.{chatRoomId}")
    public void enterUser(@Payload ChatDTO chat, @DestinationVariable String chatRoomId) {

        // 채팅방에 유저 추가
        chatService.enterRoom(chat.getRoomId(), chat.getSender());

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

    //기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatDTO chatDTO){
        log.info("received : " + chatDTO.getMessage());
        Chat chat = rootConfig.getMapper().map(chatDTO,Chat.class);
        chatRepository.save(chat);
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