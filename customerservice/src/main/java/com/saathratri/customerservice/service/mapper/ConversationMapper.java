package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.service.dto.ConversationDTO;
import com.saathratri.customerservice.service.dto.EndDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversation} and its DTO {@link ConversationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConversationMapper extends EntityMapper<ConversationDTO, Conversation> {
    @Mapping(target = "end", source = "end", qualifiedByName = "endId")
    ConversationDTO toDto(Conversation s);

    @Named("endId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EndDTO toDtoEndId(End end);
}
