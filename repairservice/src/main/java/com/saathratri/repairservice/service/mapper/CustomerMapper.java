package com.saathratri.repairservice.service.mapper;

import com.saathratri.repairservice.domain.Customer;
import com.saathratri.repairservice.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {}
