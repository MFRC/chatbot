package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.CustomerServiceEntity;
import com.saathratri.customerservice.service.dto.CustomerServiceEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerServiceEntity} and its DTO {@link CustomerServiceEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerServiceEntityMapper extends EntityMapper<CustomerServiceEntityDTO, CustomerServiceEntity> {}
