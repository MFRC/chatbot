package com.saathratri.chatbotservice.service.mapper;

import com.saathratri.chatbotservice.domain.ChatSession;
import com.saathratri.chatbotservice.domain.User;
import com.saathratri.chatbotservice.service.dto.ChatSessionDTO;
import com.saathratri.chatbotservice.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatSession} and its DTO {@link ChatSessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatSessionMapper extends EntityMapper<ChatSessionDTO, ChatSession> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ChatSessionDTO toDto(ChatSession s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
