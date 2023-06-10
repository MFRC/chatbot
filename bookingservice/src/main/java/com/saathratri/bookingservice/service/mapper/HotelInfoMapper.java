package com.saathratri.bookingservice.service.mapper;

import com.saathratri.bookingservice.domain.Address;
import com.saathratri.bookingservice.domain.HotelInfo;
import com.saathratri.bookingservice.domain.LoyaltyProgram;
import com.saathratri.bookingservice.service.dto.AddressDTO;
import com.saathratri.bookingservice.service.dto.HotelInfoDTO;
import com.saathratri.bookingservice.service.dto.LoyaltyProgramDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HotelInfo} and its DTO {@link HotelInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface HotelInfoMapper extends EntityMapper<HotelInfoDTO, HotelInfo> {
    @Mapping(target = "loyaltyProgram", source = "loyaltyProgram", qualifiedByName = "loyaltyProgramId")
    @Mapping(target = "address", source = "address", qualifiedByName = "addressId")
    HotelInfoDTO toDto(HotelInfo s);

    @Named("loyaltyProgramId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LoyaltyProgramDTO toDtoLoyaltyProgramId(LoyaltyProgram loyaltyProgram);

    @Named("addressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AddressDTO toDtoAddressId(Address address);
}
