package com.saathratri.orchestrator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
import com.saathratri.orchestrator.service.CustomerService;
import com.saathratri.orchestrator.service.MessageSenderService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
    private WebClient webClient;

	@Autowired
	private MessageSenderService messageSenderService;

	@Value("${saathratri.customer-service.api-base-url}")
    private String customerServiceApiBaseUrl;

	@Override
	public Mono<CustomerServiceUserDTO> createCustomerServiceUser(CustomerServiceUserDTO customerServiceUserDTO) {
		return webClient.post()
			.uri(customerServiceApiBaseUrl + "/customer-service-users")
			.bodyValue(customerServiceUserDTO)
			.retrieve()
			.bodyToMono(CustomerServiceUserDTO.class);
	}
}
