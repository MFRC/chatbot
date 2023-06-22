package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.*; // for static metamodels
import com.saathratri.customerservice.domain.CustomerServiceUser;
import com.saathratri.customerservice.repository.CustomerServiceUserRepository;
import com.saathratri.customerservice.service.criteria.CustomerServiceUserCriteria;
import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceUserMapper;
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
 * Service for executing complex queries for {@link CustomerServiceUser} entities in the database.
 * The main input is a {@link CustomerServiceUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerServiceUserDTO} or a {@link Page} of {@link CustomerServiceUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerServiceUserQueryService extends QueryService<CustomerServiceUser> {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceUserQueryService.class);

    private final CustomerServiceUserRepository customerServiceUserRepository;

    private final CustomerServiceUserMapper customerServiceUserMapper;

    public CustomerServiceUserQueryService(
        CustomerServiceUserRepository customerServiceUserRepository,
        CustomerServiceUserMapper customerServiceUserMapper
    ) {
        this.customerServiceUserRepository = customerServiceUserRepository;
        this.customerServiceUserMapper = customerServiceUserMapper;
    }

    /**
     * Return a {@link List} of {@link CustomerServiceUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerServiceUserDTO> findByCriteria(CustomerServiceUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerServiceUser> specification = createSpecification(criteria);
        return customerServiceUserMapper.toDto(customerServiceUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerServiceUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerServiceUserDTO> findByCriteria(CustomerServiceUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerServiceUser> specification = createSpecification(criteria);
        return customerServiceUserRepository.findAll(specification, page).map(customerServiceUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerServiceUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerServiceUser> specification = createSpecification(criteria);
        return customerServiceUserRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerServiceUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerServiceUser> createSpecification(CustomerServiceUserCriteria criteria) {
        Specification<CustomerServiceUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CustomerServiceUser_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), CustomerServiceUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), CustomerServiceUser_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerServiceUser_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CustomerServiceUser_.phoneNumber));
            }
            if (criteria.getReservationNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReservationNumber(), CustomerServiceUser_.reservationNumber));
            }
            if (criteria.getRoomNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoomNumber(), CustomerServiceUser_.roomNumber));
            }
            if (criteria.getConversationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConversationId(),
                            root -> root.join(CustomerServiceUser_.conversation, JoinType.LEFT).get(Conversation_.id)
                        )
                    );
            }
            if (criteria.getCustomerServiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerServiceId(),
                            root -> root.join(CustomerServiceUser_.customerService, JoinType.LEFT).get(CustomerService_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
