package com.saathratri.chatbotservice.repository;

import com.saathratri.chatbotservice.domain.ChatbotServiceUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatbotServiceUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatbotServiceUserRepository
    extends JpaRepository<ChatbotServiceUser, UUID>, JpaSpecificationExecutor<ChatbotServiceUser> {}
