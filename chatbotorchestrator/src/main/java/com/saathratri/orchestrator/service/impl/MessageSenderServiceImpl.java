package com.saathratri.orchestrator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.saathratri.messagesender.service.dto.TextMessageDTO;
import com.saathratri.messagesender.service.dto.WhatsAppMessageDTO;
import com.saathratri.messagesender.service.dto.AuthenticationInfoDTO;
import com.saathratri.orchestrator.service.MessageSenderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class MessageSenderServiceImpl implements MessageSenderService {

    private static final Logger log = LoggerFactory.getLogger(MessageSenderServiceImpl.class);

	@Autowired
    private WebClient webClient;

    @Value("${saathratri.message-sender-service.api-base-url}")
    private String saathratriMessageSenderServiceApiBaseUrl;

	public void addTextingRestClient(String accountPhoneNumber, String accountSid, String accountAuthToken) {
		log.debug("RESTful call to {}text/add-rest-client.", saathratriMessageSenderServiceApiBaseUrl);

		AuthenticationInfoDTO authenticationInfoDTO = new AuthenticationInfoDTO();
		authenticationInfoDTO.setAccountPhoneNumber(accountPhoneNumber);
		authenticationInfoDTO.setAccountSid(accountSid);
		authenticationInfoDTO.setAccountAuthToken(accountAuthToken);

		log.info("authenticationInfoDTO: {}", authenticationInfoDTO);

		webClient.put()
			.uri(saathratriMessageSenderServiceApiBaseUrl + "text/add-rest-client")
			.bodyValue(authenticationInfoDTO)
			.retrieve()
			.bodyToMono(Void.class)
			.doOnSuccess(response -> log.info("addTextingRestClient Message successfully sent."))
			.doOnError(error -> log.error(error.getMessage(), error))
			.subscribe(); // Subscribe to the response Mono to trigger the request
	}

	public void sendTextMessage(TextMessageDTO messageDTO) {
		log.debug("RESTful call to {}text/send-calendar-events.", saathratriMessageSenderServiceApiBaseUrl);

		webClient.put()
			.uri(saathratriMessageSenderServiceApiBaseUrl + "text/send-text-message")
			.bodyValue(messageDTO)
			.retrieve()
			.bodyToMono(Void.class)
			.doOnSuccess(response -> log.info("sendCalendarEventsMessageText Message successfully sent."))
			.doOnError(error -> log.error(error.getMessage(), error))
			.subscribe(); // Subscribe to the response Mono to trigger the request
	}

	public void addPhoneCallingRestClient(String accountPhoneNumber, String accountSid, String accountAuthToken) {
		log.debug("RESTful call to {}phone/add-rest-client.", saathratriMessageSenderServiceApiBaseUrl);

		AuthenticationInfoDTO authenticationInfoDTO = new AuthenticationInfoDTO();
		authenticationInfoDTO.setAccountPhoneNumber(accountPhoneNumber);
		authenticationInfoDTO.setAccountSid(accountSid);
		authenticationInfoDTO.setAccountAuthToken(accountAuthToken);

		log.info("authenticationInfoDTO: {}", authenticationInfoDTO);

		webClient.put()
			.uri(saathratriMessageSenderServiceApiBaseUrl + "phone/add-rest-client")
			.bodyValue(authenticationInfoDTO)
			.retrieve()
			.bodyToMono(Void.class)
			.doOnSuccess(response -> log.info("addPhoneCallingRestClient Message successfully sent."))
			.doOnError(error -> log.error(error.getMessage(), error))
			.subscribe(); // Subscribe to the response Mono to trigger the request
	}

	public Mono<Boolean> isEmailValid(String email) {
		log.debug("RESTful call to {}is-email-valid.", saathratriMessageSenderServiceApiBaseUrl);
		
		return webClient.get()
			.uri(saathratriMessageSenderServiceApiBaseUrl + "/is-email-valid?email={email}", email)
			.retrieve()
			.bodyToMono(Boolean.class);
	}

    public Mono<Boolean> isPhoneNumberValid(String toPhoneNumber, String fromPhoneNumber) {
		log.debug("RESTful call to {}is-phone-number-valid.", saathratriMessageSenderServiceApiBaseUrl);

		return webClient.get()
			.uri(saathratriMessageSenderServiceApiBaseUrl + "/is-phone-number-valid?toPhoneNumber={toPhoneNumber}&fromPhoneNumber={fromPhoneNumber}", 
				toPhoneNumber,
				fromPhoneNumber)
			.retrieve()
			.bodyToMono(Boolean.class);
	}
}