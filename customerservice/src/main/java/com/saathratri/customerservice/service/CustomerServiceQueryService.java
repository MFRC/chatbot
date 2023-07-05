package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.*; // for static metamodels
import com.saathratri.customerservice.domain.CustomerService;
import com.saathratri.customerservice.repository.CustomerServiceRepository;
import com.saathratri.customerservice.service.criteria.CustomerServiceCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceMapper;
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
 * Service for executing complex queries for {@link CustomerService} entities in the database.
 * The main input is a {@link CustomerServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerServiceDTO} or a {@link Page} of {@link CustomerServiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerServiceQueryService extends QueryService<CustomerService> {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceQueryService.class);

    private final CustomerServiceRepository customerServiceRepository;

    private final CustomerServiceMapper customerServiceMapper;

    public CustomerServiceQueryService(CustomerServiceRepository customerServiceRepository, CustomerServiceMapper customerServiceMapper) {
        this.customerServiceRepository = customerServiceRepository;
        this.customerServiceMapper = customerServiceMapper;
    }

    /**
     * Return a {@link List} of {@link CustomerServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerServiceDTO> findByCriteria(CustomerServiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerService> specification = createSpecification(criteria);
        return customerServiceMapper.toDto(customerServiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerServiceDTO> findByCriteria(CustomerServiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerService> specification = createSpecification(criteria);
        return customerServiceRepository.findAll(specification, page).map(customerServiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerServiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerService> specification = createSpecification(criteria);
        return customerServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerService> createSpecification(CustomerServiceCriteria criteria) {
        Specification<CustomerService> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CustomerService_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), CustomerService_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), CustomerService_.endDate));
            }
            if (criteria.getReportNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportNumber(), CustomerService_.reportNumber));
            }
            if (criteria.getFaqsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFaqsId(), root -> root.join(CustomerService_.faqs, JoinType.LEFT).get(FAQs_.id))
                    );
            }
            if (criteria.getCustomerServiceEntityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerServiceEntityId(),
                            root -> root.join(CustomerService_.customerServiceEntity, JoinType.LEFT).get(CustomerServiceEntity_.id)
                        )
                    );
            }
            if (criteria.getCustomerServiceUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerServiceUserId(),
                            root -> root.join(CustomerService_.customerServiceUser, JoinType.LEFT).get(CustomerServiceUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
