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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.saathratri.orchestrator.service.ChatbotService;
import com.saathratri.orchestrator.service.MessageSenderService;
import com.saathratri.orchestrator.web.rest.Utils;

@Service
@Transactional
public class ChatbotServiceImpl implements ChatbotService {

    private static final Logger log = LoggerFactory.getLogger(ChatbotServiceImpl.class);

	@Autowired
    private WebClient webClient;

	@Autowired
	private MessageSenderService messageSenderService;

	@Value("${saathratri.chatbot-service.api-base-url}")
    private String chatbotServiceApiBaseUrl;
}
