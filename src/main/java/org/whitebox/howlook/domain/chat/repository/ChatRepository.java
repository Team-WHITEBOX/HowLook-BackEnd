package org.whitebox.howlook.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.chat.entity.ChatRoom;

public interface ChatRepository extends JpaRepository<ChatRoom,String> {
}
