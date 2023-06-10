package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.CustomerServiceUser;
import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerServiceUser} and its DTO {@link CustomerServiceUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerServiceUserMapper extends EntityMapper<CustomerServiceUserDTO, CustomerServiceUser> {}
