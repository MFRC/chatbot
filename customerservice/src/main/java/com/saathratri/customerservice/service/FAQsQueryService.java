package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.*; // for static metamodels
import com.saathratri.customerservice.domain.FAQs;
import com.saathratri.customerservice.repository.FAQsRepository;
import com.saathratri.customerservice.service.criteria.FAQsCriteria;
import com.saathratri.customerservice.service.dto.FAQsDTO;
import com.saathratri.customerservice.service.mapper.FAQsMapper;
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
 * Service for executing complex queries for {@link FAQs} entities in the database.
 * The main input is a {@link FAQsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FAQsDTO} or a {@link Page} of {@link FAQsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FAQsQueryService extends QueryService<FAQs> {

    private final Logger log = LoggerFactory.getLogger(FAQsQueryService.class);

    private final FAQsRepository fAQsRepository;

    private final FAQsMapper fAQsMapper;

    public FAQsQueryService(FAQsRepository fAQsRepository, FAQsMapper fAQsMapper) {
        this.fAQsRepository = fAQsRepository;
        this.fAQsMapper = fAQsMapper;
    }

    /**
     * Return a {@link List} of {@link FAQsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FAQsDTO> findByCriteria(FAQsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FAQs> specification = createSpecification(criteria);
        return fAQsMapper.toDto(fAQsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FAQsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FAQsDTO> findByCriteria(FAQsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FAQs> specification = createSpecification(criteria);
        return fAQsRepository.findAll(specification, page).map(fAQsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FAQsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FAQs> specification = createSpecification(criteria);
        return fAQsRepository.count(specification);
    }

    /**
     * Function to convert {@link FAQsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FAQs> createSpecification(FAQsCriteria criteria) {
        Specification<FAQs> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FAQs_.id));
            }
            if (criteria.getAnswers() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnswers(), FAQs_.answers));
            }
            if (criteria.getQuestion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuestion(), FAQs_.question));
            }
            if (criteria.getKeyWords() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKeyWords(), FAQs_.keyWords));
            }
            if (criteria.getConversationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConversationId(),
                            root -> root.join(FAQs_.conversation, JoinType.LEFT).get(Conversation_.id)
                        )
                    );
            }
            if (criteria.getCustomerServiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerServiceId(),
                            root -> root.join(FAQs_.customerService, JoinType.LEFT).get(CustomerService_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
