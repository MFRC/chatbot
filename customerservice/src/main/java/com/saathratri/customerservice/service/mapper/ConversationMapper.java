package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.service.dto.ConversationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversation} and its DTO {@link ConversationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConversationMapper extends EntityMapper<ConversationDTO, Conversation> {}
