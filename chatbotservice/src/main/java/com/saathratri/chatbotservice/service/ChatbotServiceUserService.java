package com.saathratri.chatbotservice.service;

import com.saathratri.chatbotservice.domain.ChatbotServiceUser;
import com.saathratri.chatbotservice.repository.ChatbotServiceUserRepository;
import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;
import com.saathratri.chatbotservice.service.mapper.ChatbotServiceUserMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChatbotServiceUser}.
 */
@Service
@Transactional
public class ChatbotServiceUserService {

    private final Logger log = LoggerFactory.getLogger(ChatbotServiceUserService.class);

    private final ChatbotServiceUserRepository chatbotServiceUserRepository;

    private final ChatbotServiceUserMapper chatbotServiceUserMapper;

    public ChatbotServiceUserService(
        ChatbotServiceUserRepository chatbotServiceUserRepository,
        ChatbotServiceUserMapper chatbotServiceUserMapper
    ) {
        this.chatbotServiceUserRepository = chatbotServiceUserRepository;
        this.chatbotServiceUserMapper = chatbotServiceUserMapper;
    }

    /**
     * Save a chatbotServiceUser.
     *
     * @param chatbotServiceUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatbotServiceUserDTO save(ChatbotServiceUserDTO chatbotServiceUserDTO) {
        log.debug("Request to save ChatbotServiceUser : {}", chatbotServiceUserDTO);
        ChatbotServiceUser chatbotServiceUser = chatbotServiceUserMapper.toEntity(chatbotServiceUserDTO);
        chatbotServiceUser = chatbotServiceUserRepository.save(chatbotServiceUser);
        return chatbotServiceUserMapper.toDto(chatbotServiceUser);
    }

    /**
     * Update a chatbotServiceUser.
     *
     * @param chatbotServiceUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatbotServiceUserDTO update(ChatbotServiceUserDTO chatbotServiceUserDTO) {
        log.debug("Request to update ChatbotServiceUser : {}", chatbotServiceUserDTO);
        ChatbotServiceUser chatbotServiceUser = chatbotServiceUserMapper.toEntity(chatbotServiceUserDTO);
        chatbotServiceUser = chatbotServiceUserRepository.save(chatbotServiceUser);
        return chatbotServiceUserMapper.toDto(chatbotServiceUser);
    }

    /**
     * Partially update a chatbotServiceUser.
     *
     * @param chatbotServiceUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatbotServiceUserDTO> partialUpdate(ChatbotServiceUserDTO chatbotServiceUserDTO) {
        log.debug("Request to partially update ChatbotServiceUser : {}", chatbotServiceUserDTO);

        return chatbotServiceUserRepository
            .findById(chatbotServiceUserDTO.getId())
            .map(existingChatbotServiceUser -> {
                chatbotServiceUserMapper.partialUpdate(existingChatbotServiceUser, chatbotServiceUserDTO);

                return existingChatbotServiceUser;
            })
            .map(chatbotServiceUserRepository::save)
            .map(chatbotServiceUserMapper::toDto);
    }

    /**
     * Get all the chatbotServiceUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatbotServiceUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatbotServiceUsers");
        return chatbotServiceUserRepository.findAll(pageable).map(chatbotServiceUserMapper::toDto);
    }

    /**
     * Get one chatbotServiceUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatbotServiceUserDTO> findOne(UUID id) {
        log.debug("Request to get ChatbotServiceUser : {}", id);
        return chatbotServiceUserRepository.findById(id).map(chatbotServiceUserMapper::toDto);
    }

    /**
     * Delete the chatbotServiceUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ChatbotServiceUser : {}", id);
        chatbotServiceUserRepository.deleteById(id);
    }
}
