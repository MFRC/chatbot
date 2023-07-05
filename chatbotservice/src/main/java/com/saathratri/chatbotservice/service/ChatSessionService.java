package com.saathratri.chatbotservice.service;

import com.saathratri.chatbotservice.domain.ChatSession;
import com.saathratri.chatbotservice.repository.ChatSessionRepository;
import com.saathratri.chatbotservice.service.dto.ChatSessionDTO;
import com.saathratri.chatbotservice.service.mapper.ChatSessionMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChatSession}.
 */
@Service
@Transactional
public class ChatSessionService {

    private final Logger log = LoggerFactory.getLogger(ChatSessionService.class);

    private final ChatSessionRepository chatSessionRepository;

    private final ChatSessionMapper chatSessionMapper;

    public ChatSessionService(ChatSessionRepository chatSessionRepository, ChatSessionMapper chatSessionMapper) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatSessionMapper = chatSessionMapper;
    }

    /**
     * Save a chatSession.
     *
     * @param chatSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatSessionDTO save(ChatSessionDTO chatSessionDTO) {
        log.debug("Request to save ChatSession : {}", chatSessionDTO);
        ChatSession chatSession = chatSessionMapper.toEntity(chatSessionDTO);
        chatSession = chatSessionRepository.save(chatSession);
        return chatSessionMapper.toDto(chatSession);
    }

    /**
     * Update a chatSession.
     *
     * @param chatSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatSessionDTO update(ChatSessionDTO chatSessionDTO) {
        log.debug("Request to update ChatSession : {}", chatSessionDTO);
        ChatSession chatSession = chatSessionMapper.toEntity(chatSessionDTO);
        chatSession = chatSessionRepository.save(chatSession);
        return chatSessionMapper.toDto(chatSession);
    }

    /**
     * Partially update a chatSession.
     *
     * @param chatSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatSessionDTO> partialUpdate(ChatSessionDTO chatSessionDTO) {
        log.debug("Request to partially update ChatSession : {}", chatSessionDTO);

        return chatSessionRepository
            .findById(chatSessionDTO.getId())
            .map(existingChatSession -> {
                chatSessionMapper.partialUpdate(existingChatSession, chatSessionDTO);

                return existingChatSession;
            })
            .map(chatSessionRepository::save)
            .map(chatSessionMapper::toDto);
    }

    /**
     * Get all the chatSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatSessionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatSessions");
        return chatSessionRepository.findAll(pageable).map(chatSessionMapper::toDto);
    }

    /**
     * Get one chatSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatSessionDTO> findOne(UUID id) {
        log.debug("Request to get ChatSession : {}", id);
        return chatSessionRepository.findById(id).map(chatSessionMapper::toDto);
    }

    /**
     * Delete the chatSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ChatSession : {}", id);
        chatSessionRepository.deleteById(id);
    }
}
