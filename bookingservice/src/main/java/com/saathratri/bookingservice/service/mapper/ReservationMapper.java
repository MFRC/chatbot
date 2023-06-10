package com.saathratri.bookingservice.service.mapper;

import com.saathratri.bookingservice.domain.HotelInfo;
import com.saathratri.bookingservice.domain.Reservation;
import com.saathratri.bookingservice.service.dto.HotelInfoDTO;
import com.saathratri.bookingservice.service.dto.ReservationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "hotel", source = "hotel", qualifiedByName = "hotelInfoId")
    ReservationDTO toDto(Reservation s);

    @Named("hotelInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HotelInfoDTO toDtoHotelInfoId(HotelInfo hotelInfo);
}
