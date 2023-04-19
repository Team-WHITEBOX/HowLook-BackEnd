package org.whitebox.howlook.domain.chat.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.whitebox.howlook.domain.chat.entity.Chat;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends MongoRepository<Chat,String> {
    public List<Chat> findAllByRoomIdAndTimeAfter(String roomId, LocalDateTime time);
}
