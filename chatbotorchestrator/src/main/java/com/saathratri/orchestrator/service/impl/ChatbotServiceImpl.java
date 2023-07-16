package com.saathratri.orchestrator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import com.saathratri.bookingservice.service.dto.CustomerInfoDTO;
import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;
import com.saathratri.orchestrator.service.ChatbotService;
import com.saathratri.orchestrator.service.MessageSenderService;

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

	@Override
	public Mono<ChatbotServiceUserDTO> createChatbotServiceUser(ChatbotServiceUserDTO chatbotServiceUserDTO) {
		return webClient.post()
			.uri(chatbotServiceApiBaseUrl + "/chatbot-service-users")
			.bodyValue(chatbotServiceUserDTO)
			.retrieve()
			.bodyToMono(ChatbotServiceUserDTO.class);
	}
}
