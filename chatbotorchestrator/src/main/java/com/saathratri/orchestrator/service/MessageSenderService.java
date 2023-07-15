package com.saathratri.orchestrator.service;

import com.saathratri.messagesender.service.dto.TextMessageDTO;

import reactor.core.publisher.Mono;


public interface MessageSenderService {
    void addTextingRestClient(String accountPhoneNumber, String accountSid, String accountAuthToken);
    void addPhoneCallingRestClient(String accountPhoneNumber, String accountSid, String accountAuthToken);
    void sendTextMessage(TextMessageDTO messageDTO);
    Mono<Boolean> isEmailValid(String email);
    Mono<Boolean> isPhoneNumberValid(String toPhoneNumber, String fromPhoneNumber);
 }
