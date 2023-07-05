package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.domain.CustomerServiceUser;
import com.saathratri.customerservice.service.dto.ConversationDTO;
import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerServiceUser} and its DTO {@link CustomerServiceUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerServiceUserMapper extends EntityMapper<CustomerServiceUserDTO, CustomerServiceUser> {
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    CustomerServiceUserDTO toDto(CustomerServiceUser s);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);
}
