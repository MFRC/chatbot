package com.saathratri.repairservice.service;

import com.saathratri.repairservice.domain.*; // for static metamodels
import com.saathratri.repairservice.domain.RepairRequest;
import com.saathratri.repairservice.repository.RepairRequestRepository;
import com.saathratri.repairservice.service.criteria.RepairRequestCriteria;
import com.saathratri.repairservice.service.dto.RepairRequestDTO;
import com.saathratri.repairservice.service.mapper.RepairRequestMapper;
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
 * Service for executing complex queries for {@link RepairRequest} entities in the database.
 * The main input is a {@link RepairRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RepairRequestDTO} or a {@link Page} of {@link RepairRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RepairRequestQueryService extends QueryService<RepairRequest> {

    private final Logger log = LoggerFactory.getLogger(RepairRequestQueryService.class);

    private final RepairRequestRepository repairRequestRepository;

    private final RepairRequestMapper repairRequestMapper;

    public RepairRequestQueryService(RepairRequestRepository repairRequestRepository, RepairRequestMapper repairRequestMapper) {
        this.repairRequestRepository = repairRequestRepository;
        this.repairRequestMapper = repairRequestMapper;
    }

    /**
     * Return a {@link List} of {@link RepairRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RepairRequestDTO> findByCriteria(RepairRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RepairRequest> specification = createSpecification(criteria);
        return repairRequestMapper.toDto(repairRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RepairRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RepairRequestDTO> findByCriteria(RepairRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RepairRequest> specification = createSpecification(criteria);
        return repairRequestRepository.findAll(specification, page).map(repairRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RepairRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RepairRequest> specification = createSpecification(criteria);
        return repairRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link RepairRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RepairRequest> createSpecification(RepairRequestCriteria criteria) {
        Specification<RepairRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RepairRequest_.id));
            }
            if (criteria.getRepairRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRepairRequestId(), RepairRequest_.repairRequestId));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(), RepairRequest_.customerId));
            }
            if (criteria.getRoomNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoomNumber(), RepairRequest_.roomNumber));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), RepairRequest_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), RepairRequest_.status));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), RepairRequest_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), RepairRequest_.dateUpdated));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(RepairRequest_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
