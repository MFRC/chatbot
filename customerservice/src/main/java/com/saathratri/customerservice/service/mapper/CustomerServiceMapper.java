package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.CustomerService;
import com.saathratri.customerservice.service.dto.CustomerServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerService} and its DTO {@link CustomerServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerServiceMapper extends EntityMapper<CustomerServiceDTO, CustomerService> {}
