package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.CustomerServiceEntity;
import com.saathratri.customerservice.service.dto.ConversationDTO;
import com.saathratri.customerservice.service.dto.CustomerServiceEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerServiceEntity} and its DTO {@link CustomerServiceEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerServiceEntityMapper extends EntityMapper<CustomerServiceEntityDTO, CustomerServiceEntity> {
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    CustomerServiceEntityDTO toDto(CustomerServiceEntity s);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);
}
