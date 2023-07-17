package com.saathratri.orchestrator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import com.saathratri.orchestrator.service.RepairService;
import com.saathratri.repairservice.service.dto.CustomerDTO;

@Service
@Transactional
public class RepairServiceImpl implements RepairService {

    private static final Logger log = LoggerFactory.getLogger(RepairServiceImpl.class);

	@Autowired
    private WebClient webClient;

	@Value("${saathratri.repair-service.api-base-url}")
    private String repairServiceApiBaseUrl;

	@Override
    public Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        return webClient.post()
			.uri(repairServiceApiBaseUrl + "/customers")
			.bodyValue(customerDTO)
			.retrieve()
			.bodyToMono(CustomerDTO.class);
    }
}
