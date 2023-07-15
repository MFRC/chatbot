package com.saathratri.orchestrator.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.saathratri.messagesender.service.dto.WhatsAppMessageDTO;
import com.saathratri.orchestrator.service.MessageSenderService;
import com.saathratri.orchestrator.web.rest.errors.BadRequestAlertException;

import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Source: https://github.com/jhipster/jhipster-sample-app-cassandra/blob/main/src/main/java/io/github/jhipster/sample/service/MailService.java
 */
@RestController
@RequestMapping("/api")
public class MessageSenderResource {

    private static final Logger log = LoggerFactory.getLogger(MessageSenderResource.class);

    private static final String CALL_STATUS_RINGING = "ringing";
    private static final String CALL_STATUS_NO_ANSWER = "no-answer";
    private static final String CALL_STATUS_COMPLETED = "completed";

    @Autowired
    private MessageSenderService messageSenderService;

    @GetMapping("/message-sender/is-email-valid")
    Mono<Boolean> isEmailValid(
        @RequestParam("email") String email
    ){
        log.debug("REST request to validate the email: {}", email);

        return messageSenderService.isEmailValid(email);
    }

    @GetMapping("/message-sender/is-phone-number-valid")
    Mono<Boolean> isPhoneNumberValid(
        @RequestParam("toPhoneNumber") String toPhoneNumber,
        @RequestParam("fromPhoneNumber") String fromPhoneNumber
    ){
        log.debug("REST request to validate the to phone number '{}' and from phone number '{}'.", toPhoneNumber, fromPhoneNumber);
        
        return messageSenderService.isPhoneNumberValid(toPhoneNumber, fromPhoneNumber);
    }
}
