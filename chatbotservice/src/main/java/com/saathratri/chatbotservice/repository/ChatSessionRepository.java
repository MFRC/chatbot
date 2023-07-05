package com.saathratri.chatbotservice.repository;

import com.saathratri.chatbotservice.domain.ChatSession;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID>, JpaSpecificationExecutor<ChatSession> {
    @Query("select chatSession from ChatSession chatSession where chatSession.user.login = ?#{principal.preferredUsername}")
    List<ChatSession> findByUserIsCurrentUser();
}
