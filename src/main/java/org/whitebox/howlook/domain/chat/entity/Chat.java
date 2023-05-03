package org.whitebox.howlook.domain.chat.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.whitebox.howlook.domain.chat.dto.ChatDTO;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Document("chat") //mongoDB
public class Chat {
    @Id
    private String id;
    private ChatDTO.MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String message; // 메시지
    private LocalDateTime time; // 채팅 발송 시간간
}
