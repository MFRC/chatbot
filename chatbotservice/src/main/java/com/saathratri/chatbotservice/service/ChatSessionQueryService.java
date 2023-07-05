package com.saathratri.chatbotservice.service;

import com.saathratri.chatbotservice.domain.*; // for static metamodels
import com.saathratri.chatbotservice.domain.ChatSession;
import com.saathratri.chatbotservice.repository.ChatSessionRepository;
import com.saathratri.chatbotservice.service.criteria.ChatSessionCriteria;
import com.saathratri.chatbotservice.service.dto.ChatSessionDTO;
import com.saathratri.chatbotservice.service.mapper.ChatSessionMapper;
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
 * Service for executing complex queries for {@link ChatSession} entities in the database.
 * The main input is a {@link ChatSessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatSessionDTO} or a {@link Page} of {@link ChatSessionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatSessionQueryService extends QueryService<ChatSession> {

    private final Logger log = LoggerFactory.getLogger(ChatSessionQueryService.class);

    private final ChatSessionRepository chatSessionRepository;

    private final ChatSessionMapper chatSessionMapper;

    public ChatSessionQueryService(ChatSessionRepository chatSessionRepository, ChatSessionMapper chatSessionMapper) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatSessionMapper = chatSessionMapper;
    }

    /**
     * Return a {@link List} of {@link ChatSessionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatSessionDTO> findByCriteria(ChatSessionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatSession> specification = createSpecification(criteria);
        return chatSessionMapper.toDto(chatSessionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatSessionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatSessionDTO> findByCriteria(ChatSessionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatSession> specification = createSpecification(criteria);
        return chatSessionRepository.findAll(specification, page).map(chatSessionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatSessionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatSession> specification = createSpecification(criteria);
        return chatSessionRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatSessionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatSession> createSpecification(ChatSessionCriteria criteria) {
        Specification<ChatSession> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatSession_.id));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), ChatSession_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), ChatSession_.endTime));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(ChatSession_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
