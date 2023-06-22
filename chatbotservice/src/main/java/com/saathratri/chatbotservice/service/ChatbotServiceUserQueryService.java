package com.saathratri.chatbotservice.service;

import com.saathratri.chatbotservice.domain.*; // for static metamodels
import com.saathratri.chatbotservice.domain.ChatbotServiceUser;
import com.saathratri.chatbotservice.repository.ChatbotServiceUserRepository;
import com.saathratri.chatbotservice.service.criteria.ChatbotServiceUserCriteria;
import com.saathratri.chatbotservice.service.dto.ChatbotServiceUserDTO;
import com.saathratri.chatbotservice.service.mapper.ChatbotServiceUserMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ChatbotServiceUser} entities in the database.
 * The main input is a {@link ChatbotServiceUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatbotServiceUserDTO} or a {@link Page} of {@link ChatbotServiceUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatbotServiceUserQueryService extends QueryService<ChatbotServiceUser> {

    private final Logger log = LoggerFactory.getLogger(ChatbotServiceUserQueryService.class);

    private final ChatbotServiceUserRepository chatbotServiceUserRepository;

    private final ChatbotServiceUserMapper chatbotServiceUserMapper;

    public ChatbotServiceUserQueryService(
        ChatbotServiceUserRepository chatbotServiceUserRepository,
        ChatbotServiceUserMapper chatbotServiceUserMapper
    ) {
        this.chatbotServiceUserRepository = chatbotServiceUserRepository;
        this.chatbotServiceUserMapper = chatbotServiceUserMapper;
    }

    /**
     * Return a {@link List} of {@link ChatbotServiceUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatbotServiceUserDTO> findByCriteria(ChatbotServiceUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatbotServiceUser> specification = createSpecification(criteria);
        return chatbotServiceUserMapper.toDto(chatbotServiceUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatbotServiceUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatbotServiceUserDTO> findByCriteria(ChatbotServiceUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatbotServiceUser> specification = createSpecification(criteria);
        return chatbotServiceUserRepository.findAll(specification, page).map(chatbotServiceUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatbotServiceUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatbotServiceUser> specification = createSpecification(criteria);
        return chatbotServiceUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatbotServiceUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatbotServiceUser> createSpecification(ChatbotServiceUserCriteria criteria) {
        Specification<ChatbotServiceUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatbotServiceUser_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ChatbotServiceUser_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), ChatbotServiceUser_.email));
            }
        }
        return specification;
    }
}
