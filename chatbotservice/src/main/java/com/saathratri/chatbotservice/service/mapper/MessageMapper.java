package com.saathratri.chatbotservice.service.mapper;

import com.saathratri.chatbotservice.domain.ChatSession;
import com.saathratri.chatbotservice.domain.Message;
import com.saathratri.chatbotservice.domain.User;
import com.saathratri.chatbotservice.service.dto.ChatSessionDTO;
import com.saathratri.chatbotservice.service.dto.MessageDTO;
import com.saathratri.chatbotservice.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "chatSession", source = "chatSession", qualifiedByName = "chatSessionId")
    MessageDTO toDto(Message s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("chatSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatSessionDTO toDtoChatSessionId(ChatSession chatSession);
}
