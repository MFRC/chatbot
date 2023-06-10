package com.saathratri.chatbotservice.service.mapper;

import com.saathratri.chatbotservice.domain.ChatbotServiceUser;
import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatbotServiceUser} and its DTO {@link ChatbotServiceUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatbotServiceUserMapper extends EntityMapper<ChatbotServiceUserDTO, ChatbotServiceUser> {}
