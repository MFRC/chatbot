package com.saathratri.repairservice.service.mapper;

import com.saathratri.repairservice.domain.Customer;
import com.saathratri.repairservice.domain.Payment;
import com.saathratri.repairservice.service.dto.CustomerDTO;
import com.saathratri.repairservice.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    PaymentDTO toDto(Payment s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
