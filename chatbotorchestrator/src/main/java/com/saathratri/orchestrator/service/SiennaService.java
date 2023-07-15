package com.saathratri.orchestrator.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.saathratri.sienna.service.dto.CustomerCommunicationPhoneOptInDTO;
import com.saathratri.sienna.service.dto.CustomerReservationByHotelAndAccountDTO;
import com.saathratri.siennaservice.service.dto.PhoneOptInDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SiennaService {
    ResponseEntity<String> createCustomerReservationByHotelAndAccount(CustomerReservationByHotelAndAccountDTO customerReservationByHotelAndAccountDTO);
    Flux<CustomerReservationByHotelAndAccountDTO> findCustomerReservationByHotelAndAccountByIdHotelId(UUID hotelId);
    Flux<CustomerReservationByHotelAndAccountDTO> findCustomerReservationByHotelAndAccountByIdHotelIdAndIdYearOfDateAdded(UUID hotelId, Long yearOfDateAdded);
    Flux<CustomerReservationByHotelAndAccountDTO> findCustomerReservationByHotelAndAccountByIdHotelIdAndIdYearOfDateAddedGreaterThan(UUID hotelId, Long yearOfDateAdded);
    Flux<CustomerReservationByHotelAndAccountDTO> findCustomerReservationByHotelAndAccountByIdHotelIdAndIdYearOfDateAddedLessThan(UUID hotelId, Long yearOfDateAdded);
    Mono<CustomerCommunicationPhoneOptInDTO> createCustomerCommunicationPhoneOptIn(UUID hotelId, PhoneOptInDTO phoneOptInDTO);
}