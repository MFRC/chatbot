package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.*; // for static metamodels
import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.repository.EndRepository;
import com.saathratri.customerservice.service.criteria.EndCriteria;
import com.saathratri.customerservice.service.dto.EndDTO;
import com.saathratri.customerservice.service.mapper.EndMapper;
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
 * Service for executing complex queries for {@link End} entities in the database.
 * The main input is a {@link EndCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EndDTO} or a {@link Page} of {@link EndDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EndQueryService extends QueryService<End> {

    private final Logger log = LoggerFactory.getLogger(EndQueryService.class);

    private final EndRepository endRepository;

    private final EndMapper endMapper;

    public EndQueryService(EndRepository endRepository, EndMapper endMapper) {
        this.endRepository = endRepository;
        this.endMapper = endMapper;
    }

    /**
     * Return a {@link List} of {@link EndDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EndDTO> findByCriteria(EndCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<End> specification = createSpecification(criteria);
        return endMapper.toDto(endRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EndDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EndDTO> findByCriteria(EndCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<End> specification = createSpecification(criteria);
        return endRepository.findAll(specification, page).map(endMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EndCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<End> specification = createSpecification(criteria);
        return endRepository.count(specification);
    }

    /**
     * Function to convert {@link EndCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<End> createSpecification(EndCriteria criteria) {
        Specification<End> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), End_.id));
            }
            if (criteria.getCloseMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCloseMessage(), End_.closeMessage));
            }
            if (criteria.getMoreHelp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMoreHelp(), End_.moreHelp));
            }
            if (criteria.getReportId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReportId(), root -> root.join(End_.report, JoinType.LEFT).get(Report_.id))
                    );
            }
            if (criteria.getConversationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConversationId(),
                            root -> root.join(End_.conversation, JoinType.LEFT).get(Conversation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
