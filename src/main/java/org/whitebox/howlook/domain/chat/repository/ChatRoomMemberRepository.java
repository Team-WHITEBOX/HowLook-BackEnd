package org.whitebox.howlook.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.chat.entity.ChatRoom;
import org.whitebox.howlook.domain.chat.entity.ChatRoomMember;
import org.whitebox.howlook.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember,Long> {
    Optional<ChatRoomMember> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    List<ChatRoomMember> findAllByChatRoom(ChatRoom chatRoom);

    boolean existsByChatRoomAndMember(ChatRoom chatRoom, Member member);
}
