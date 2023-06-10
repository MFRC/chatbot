package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.*; // for static metamodels
import com.saathratri.customerservice.domain.Conversation;
import com.saathratri.customerservice.repository.ConversationRepository;
import com.saathratri.customerservice.service.criteria.ConversationCriteria;
import com.saathratri.customerservice.service.dto.ConversationDTO;
import com.saathratri.customerservice.service.mapper.ConversationMapper;
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
 * Service for executing complex queries for {@link Conversation} entities in the database.
 * The main input is a {@link ConversationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConversationDTO} or a {@link Page} of {@link ConversationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConversationQueryService extends QueryService<Conversation> {

    private final Logger log = LoggerFactory.getLogger(ConversationQueryService.class);

    private final ConversationRepository conversationRepository;

    private final ConversationMapper conversationMapper;

    public ConversationQueryService(ConversationRepository conversationRepository, ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
    }

    /**
     * Return a {@link List} of {@link ConversationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConversationDTO> findByCriteria(ConversationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Conversation> specification = createSpecification(criteria);
        return conversationMapper.toDto(conversationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConversationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConversationDTO> findByCriteria(ConversationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Conversation> specification = createSpecification(criteria);
        return conversationRepository.findAll(specification, page).map(conversationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConversationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Conversation> specification = createSpecification(criteria);
        return conversationRepository.count(specification);
    }

    /**
     * Function to convert {@link ConversationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Conversation> createSpecification(ConversationCriteria criteria) {
        Specification<Conversation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Conversation_.id));
            }
        }
        return specification;
    }
}
