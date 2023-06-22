package com.saathratri.bookingservice.service.mapper;

import com.saathratri.bookingservice.domain.Address;
import com.saathratri.bookingservice.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {}
