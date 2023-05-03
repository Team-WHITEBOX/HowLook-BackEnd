package org.whitebox.howlook.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.whitebox.howlook.domain.chat.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {
    Optional<ChatRoom> findByRoomName(@Param("roomName") String roomName);
}
