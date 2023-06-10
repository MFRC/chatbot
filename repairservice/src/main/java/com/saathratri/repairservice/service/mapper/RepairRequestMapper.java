package com.saathratri.repairservice.service.mapper;

import com.saathratri.repairservice.domain.Customer;
import com.saathratri.repairservice.domain.RepairRequest;
import com.saathratri.repairservice.service.dto.CustomerDTO;
import com.saathratri.repairservice.service.dto.RepairRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RepairRequest} and its DTO {@link RepairRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface RepairRequestMapper extends EntityMapper<RepairRequestDTO, RepairRequest> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    RepairRequestDTO toDto(RepairRequest s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
