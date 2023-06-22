package com.saathratri.chatbotservice.repository;

import com.saathratri.chatbotservice.domain.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, UUID>, JpaSpecificationExecutor<Message> {
    @Query("select message from Message message where message.user.login = ?#{principal.preferredUsername}")
    List<Message> findByUserIsCurrentUser();
}
