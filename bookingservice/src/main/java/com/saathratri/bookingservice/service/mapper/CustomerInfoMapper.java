package com.saathratri.bookingservice.service.mapper;

import com.saathratri.bookingservice.domain.Address;
import com.saathratri.bookingservice.domain.CustomerInfo;
import com.saathratri.bookingservice.service.dto.AddressDTO;
import com.saathratri.bookingservice.service.dto.CustomerInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerInfo} and its DTO {@link CustomerInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerInfoMapper extends EntityMapper<CustomerInfoDTO, CustomerInfo> {
    @Mapping(target = "address", source = "address", qualifiedByName = "addressId")
    CustomerInfoDTO toDto(CustomerInfo s);

    @Named("addressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AddressDTO toDtoAddressId(Address address);
}
