package com.saathratri.orchestrator.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
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
