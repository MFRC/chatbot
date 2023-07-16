package com.saathratri.orchestrator.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;
import com.saathratri.orchestrator.service.ChatbotService;
import com.saathratri.repairservice.service.dto.CustomerDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
 * Source: https://github.com/jhipster/jhipster-sample-app-cassandra/blob/main/src/main/java/io/github/jhipster/sample/service/MailService.java
 */
@RestController
@RequestMapping("/api")
public class ChatbotServiceResource {

    private static final Logger log = LoggerFactory.getLogger(ChatbotServiceResource.class);

    @Autowired
    private ChatbotService chatbotService;

    /**
     * {@code GET  /chatbot-servicee/helloWorld} : get hello world message.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the hello world message in body.
     */
    @GetMapping("/chatbot-service/hello-world")
    public ResponseEntity<String> getHelloWorld() {
        log.debug("REST request to the hello world message from Chatbot Orchestrator's Chatbot Service.");
        
        return ResponseEntity.ok().body("Hello world!");
    }

    @PostMapping("/chatbot-service/chatbot-service-users")
    public Mono<ChatbotServiceUserDTO> createChatbotServiceUserDTO(ChatbotServiceUserDTO chatbotServiceUserDTO) {
         return chatbotService.createChatbotServiceUser(chatbotServiceUserDTO);
    }
}
