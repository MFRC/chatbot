package com.saathratri.orchestrator.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;
import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
import com.saathratri.orchestrator.service.ChatbotService;
import com.saathratri.orchestrator.service.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
 * Source: https://github.com/jhipster/jhipster-sample-app-cassandra/blob/main/src/main/java/io/github/jhipster/sample/service/MailService.java
 */
@RestController
@RequestMapping("/api")
public class CustomerServiceResource {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceResource.class);

    @Autowired
    private CustomerService customerService;

    /**
     * {@code GET  /customer-servicee/helloWorld} : get hello world message.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the hello world message in body.
     */
    @GetMapping("/customer-service/hello-world")
    public ResponseEntity<String> getHelloWorld() {
        log.debug("REST request to the hello world message from Chatbot Orchestrator's Customer Service.");
        
        return ResponseEntity.ok().body("Hello world!");
    }

    @PostMapping("/customer-service/customer-service-users")
    public Mono<CustomerServiceUserDTO> createChatbotServiceUserDTO(@RequestBody(required = true) CustomerServiceUserDTO customerServiceUserDTO) {
         return customerService.createCustomerServiceUser(customerServiceUserDTO);
    }
}
