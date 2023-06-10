package com.saathratri.repairservice.service.mapper;

import com.saathratri.repairservice.domain.Booking;
import com.saathratri.repairservice.domain.Customer;
import com.saathratri.repairservice.domain.Payment;
import com.saathratri.repairservice.service.dto.BookingDTO;
import com.saathratri.repairservice.service.dto.CustomerDTO;
import com.saathratri.repairservice.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    BookingDTO toDto(Booking s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
