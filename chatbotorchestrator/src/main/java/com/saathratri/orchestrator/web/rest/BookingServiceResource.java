package com.saathratri.orchestrator.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saathratri.bookingservice.service.dto.CustomerInfoDTO;
import com.saathratri.orchestrator.service.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
 * Source: https://github.com/jhipster/jhipster-sample-app-cassandra/blob/main/src/main/java/io/github/jhipster/sample/service/MailService.java
 */
@RestController
@RequestMapping("/api")
public class BookingServiceResource {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceResource.class);

    @Autowired
    private BookingService bookingService;

    /**
     * {@code GET  /booking-service/helloWorld} : get hello world message.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the hello world message in body.
     */
    @GetMapping("/booking-service/hello-world")
    public ResponseEntity<String> getHelloWorld() {
        log.debug("REST request to the hello world message from Chatbot Orchestrator's Booking Service.");

        return ResponseEntity.ok().body("Hello world!");
    }

    @PostMapping("/booking-service/customer-infos")
    public Mono<CustomerInfoDTO> createCustomerInfo(@RequestBody(required = true) CustomerInfoDTO customerInfoDTO) {
         return bookingService.createCustomerInfo(customerInfoDTO);
    }
}
