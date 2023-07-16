package com.saathratri.orchestrator.service;

import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;

import reactor.core.publisher.Mono;

// Reference: https://howtodoinjava.com/spring-webflux/webclient-get-post-example/
public interface CustomerService {
    Mono<CustomerServiceUserDTO> createCustomerServiceUser(CustomerServiceUserDTO customerServiceUserDTO);
}
