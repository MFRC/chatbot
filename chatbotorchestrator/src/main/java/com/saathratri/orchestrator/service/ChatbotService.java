package com.saathratri.orchestrator.service;

import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;

import reactor.core.publisher.Mono;

// Reference: https://howtodoinjava.com/spring-webflux/webclient-get-post-example/
public interface ChatbotService {
    Mono<ChatbotServiceUserDTO> createChatbotServiceUser(ChatbotServiceUserDTO chatbotServiceUserDTO);
}
