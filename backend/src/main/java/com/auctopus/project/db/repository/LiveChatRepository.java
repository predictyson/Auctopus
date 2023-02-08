package com.auctopus.project.db.repository;

import com.auctopus.project.db.domain.LiveChat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChatRepository extends JpaRepository<LiveChat, Integer> {

    List<LiveChat> getLiveChatByLiveSeq(int liveSeq);

}
