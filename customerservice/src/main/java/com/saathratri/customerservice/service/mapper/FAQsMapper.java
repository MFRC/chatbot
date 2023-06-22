package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.FAQs;
import com.saathratri.customerservice.service.dto.ConversationDTO;
import com.saathratri.customerservice.service.dto.FAQsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FAQs} and its DTO {@link FAQsDTO}.
 */
@Mapper(componentModel = "spring")
public interface FAQsMapper extends EntityMapper<FAQsDTO, FAQs> {
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    FAQsDTO toDto(FAQs s);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);
}
