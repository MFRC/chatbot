package com.saathratri.orchestrator.service;

import com.saathratri.bookingservice.service.dto.CustomerInfoDTO;

import reactor.core.publisher.Mono;

// Reference: https://howtodoinjava.com/spring-webflux/webclient-get-post-example/
public interface BookingService {
    Mono<CustomerInfoDTO> createCustomerInfo(CustomerInfoDTO customerInfoDTO);
}
