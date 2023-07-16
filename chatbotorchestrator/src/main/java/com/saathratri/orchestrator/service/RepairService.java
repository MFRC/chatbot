package com.saathratri.orchestrator.service;

import com.saathratri.repairservice.service.dto.CustomerDTO;

import reactor.core.publisher.Mono;

// Reference: https://howtodoinjava.com/spring-webflux/webclient-get-post-example/
public interface RepairService {
    Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO);
}
