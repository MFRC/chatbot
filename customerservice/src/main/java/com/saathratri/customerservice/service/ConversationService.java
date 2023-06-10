package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.repository.ConversationRepository;
import com.saathratri.customerservice.service.dto.ConversationDTO;
import com.saathratri.customerservice.service.mapper.ConversationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conversation}.
 */
@Service
@Transactional
public class ConversationService {

    private final Logger log = LoggerFactory.getLogger(ConversationService.class);

    private final ConversationRepository conversationRepository;

    private final ConversationMapper conversationMapper;

    public ConversationService(ConversationRepository conversationRepository, ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
    }

    /**
     * Save a conversation.
     *
     * @param conversationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConversationDTO save(ConversationDTO conversationDTO) {
        log.debug("Request to save Conversation : {}", conversationDTO);
        Conversation conversation = conversationMapper.toEntity(conversationDTO);
        conversation = conversationRepository.save(conversation);
        return conversationMapper.toDto(conversation);
    }

    /**
     * Update a conversation.
     *
     * @param conversationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConversationDTO update(ConversationDTO conversationDTO) {
        log.debug("Request to update Conversation : {}", conversationDTO);
        Conversation conversation = conversationMapper.toEntity(conversationDTO);
        conversation = conversationRepository.save(conversation);
        return conversationMapper.toDto(conversation);
    }

    /**
     * Partially update a conversation.
     *
     * @param conversationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConversationDTO> partialUpdate(ConversationDTO conversationDTO) {
        log.debug("Request to partially update Conversation : {}", conversationDTO);

        return conversationRepository
            .findById(conversationDTO.getId())
            .map(existingConversation -> {
                conversationMapper.partialUpdate(existingConversation, conversationDTO);

                return existingConversation;
            })
            .map(conversationRepository::save)
            .map(conversationMapper::toDto);
    }

    /**
     * Get all the conversations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConversationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conversations");
        return conversationRepository.findAll(pageable).map(conversationMapper::toDto);
    }

    /**
     *  Get all the conversations where Faqs is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConversationDTO> findAllWhereFaqsIsNull() {
        log.debug("Request to get all conversations where Faqs is null");
        return StreamSupport
            .stream(conversationRepository.findAll().spliterator(), false)
            .filter(conversation -> conversation.getFaqs() == null)
            .map(conversationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the conversations where CustomerServiceEntity is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConversationDTO> findAllWhereCustomerServiceEntityIsNull() {
        log.debug("Request to get all conversations where CustomerServiceEntity is null");
        return StreamSupport
            .stream(conversationRepository.findAll().spliterator(), false)
            .filter(conversation -> conversation.getCustomerServiceEntity() == null)
            .map(conversationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the conversations where CustomerServiceUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConversationDTO> findAllWhereCustomerServiceUserIsNull() {
        log.debug("Request to get all conversations where CustomerServiceUser is null");
        return StreamSupport
            .stream(conversationRepository.findAll().spliterator(), false)
            .filter(conversation -> conversation.getCustomerServiceUser() == null)
            .map(conversationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one conversation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConversationDTO> findOne(UUID id) {
        log.debug("Request to get Conversation : {}", id);
        return conversationRepository.findById(id).map(conversationMapper::toDto);
    }

    /**
     * Delete the conversation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Conversation : {}", id);
        conversationRepository.deleteById(id);
    }
}
