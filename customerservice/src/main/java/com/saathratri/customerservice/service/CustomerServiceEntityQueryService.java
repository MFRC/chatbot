package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.*; // for static metamodels
import com.saathratri.customerservice.domain.CustomerServiceEntity;
import com.saathratri.customerservice.repository.CustomerServiceEntityRepository;
import com.saathratri.customerservice.service.criteria.CustomerServiceEntityCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceEntityDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceEntityMapper;
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
 * Service for executing complex queries for {@link CustomerServiceEntity} entities in the database.
 * The main input is a {@link CustomerServiceEntityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerServiceEntityDTO} or a {@link Page} of {@link CustomerServiceEntityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerServiceEntityQueryService extends QueryService<CustomerServiceEntity> {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceEntityQueryService.class);

    private final CustomerServiceEntityRepository customerServiceEntityRepository;

    private final CustomerServiceEntityMapper customerServiceEntityMapper;

    public CustomerServiceEntityQueryService(
        CustomerServiceEntityRepository customerServiceEntityRepository,
        CustomerServiceEntityMapper customerServiceEntityMapper
    ) {
        this.customerServiceEntityRepository = customerServiceEntityRepository;
        this.customerServiceEntityMapper = customerServiceEntityMapper;
    }

    /**
     * Return a {@link List} of {@link CustomerServiceEntityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerServiceEntityDTO> findByCriteria(CustomerServiceEntityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerServiceEntity> specification = createSpecification(criteria);
        return customerServiceEntityMapper.toDto(customerServiceEntityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerServiceEntityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerServiceEntityDTO> findByCriteria(CustomerServiceEntityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerServiceEntity> specification = createSpecification(criteria);
        return customerServiceEntityRepository.findAll(specification, page).map(customerServiceEntityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerServiceEntityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerServiceEntity> specification = createSpecification(criteria);
        return customerServiceEntityRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerServiceEntityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerServiceEntity> createSpecification(CustomerServiceEntityCriteria criteria) {
        Specification<CustomerServiceEntity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CustomerServiceEntity_.id));
            }
            if (criteria.getReservationNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReservationNumber(), CustomerServiceEntity_.reservationNumber));
            }
            if (criteria.getRoomNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRoomNumber(), CustomerServiceEntity_.roomNumber));
            }
            if (criteria.getServices() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServices(), CustomerServiceEntity_.services));
            }
            if (criteria.getPrices() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrices(), CustomerServiceEntity_.prices));
            }
            if (criteria.getAmenities() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAmenities(), CustomerServiceEntity_.amenities));
            }
            if (criteria.getConversationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConversationId(),
                            root -> root.join(CustomerServiceEntity_.conversation, JoinType.LEFT).get(Conversation_.id)
                        )
                    );
            }
            if (criteria.getCustomerServiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerServiceId(),
                            root -> root.join(CustomerServiceEntity_.customerService, JoinType.LEFT).get(CustomerService_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
