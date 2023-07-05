package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.CustomerService;
import com.saathratri.customerservice.domain.CustomerServiceEntity;
import com.saathratri.customerservice.domain.CustomerServiceUser;
import com.saathratri.customerservice.domain.FAQs;
import com.saathratri.customerservice.service.dto.CustomerServiceDTO;
import com.saathratri.customerservice.service.dto.CustomerServiceEntityDTO;
import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
import com.saathratri.customerservice.service.dto.FAQsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerService} and its DTO {@link CustomerServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerServiceMapper extends EntityMapper<CustomerServiceDTO, CustomerService> {
    @Mapping(target = "faqs", source = "faqs", qualifiedByName = "fAQsId")
    @Mapping(target = "customerServiceEntity", source = "customerServiceEntity", qualifiedByName = "customerServiceEntityId")
    @Mapping(target = "customerServiceUser", source = "customerServiceUser", qualifiedByName = "customerServiceUserId")
    CustomerServiceDTO toDto(CustomerService s);

    @Named("fAQsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FAQsDTO toDtoFAQsId(FAQs fAQs);

    @Named("customerServiceEntityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerServiceEntityDTO toDtoCustomerServiceEntityId(CustomerServiceEntity customerServiceEntity);

    @Named("customerServiceUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerServiceUserDTO toDtoCustomerServiceUserId(CustomerServiceUser customerServiceUser);
}
