package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.CustomerServiceEntity;
import com.saathratri.customerservice.repository.CustomerServiceEntityRepository;
import com.saathratri.customerservice.service.dto.CustomerServiceEntityDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceEntityMapper;
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
 * Service Implementation for managing {@link CustomerServiceEntity}.
 */
@Service
@Transactional
public class CustomerServiceEntityService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceEntityService.class);

    private final CustomerServiceEntityRepository customerServiceEntityRepository;

    private final CustomerServiceEntityMapper customerServiceEntityMapper;

    public CustomerServiceEntityService(
        CustomerServiceEntityRepository customerServiceEntityRepository,
        CustomerServiceEntityMapper customerServiceEntityMapper
    ) {
        this.customerServiceEntityRepository = customerServiceEntityRepository;
        this.customerServiceEntityMapper = customerServiceEntityMapper;
    }

    /**
     * Save a customerServiceEntity.
     *
     * @param customerServiceEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerServiceEntityDTO save(CustomerServiceEntityDTO customerServiceEntityDTO) {
        log.debug("Request to save CustomerServiceEntity : {}", customerServiceEntityDTO);
        CustomerServiceEntity customerServiceEntity = customerServiceEntityMapper.toEntity(customerServiceEntityDTO);
        customerServiceEntity = customerServiceEntityRepository.save(customerServiceEntity);
        return customerServiceEntityMapper.toDto(customerServiceEntity);
    }

    /**
     * Update a customerServiceEntity.
     *
     * @param customerServiceEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerServiceEntityDTO update(CustomerServiceEntityDTO customerServiceEntityDTO) {
        log.debug("Request to update CustomerServiceEntity : {}", customerServiceEntityDTO);
        CustomerServiceEntity customerServiceEntity = customerServiceEntityMapper.toEntity(customerServiceEntityDTO);
        customerServiceEntity = customerServiceEntityRepository.save(customerServiceEntity);
        return customerServiceEntityMapper.toDto(customerServiceEntity);
    }

    /**
     * Partially update a customerServiceEntity.
     *
     * @param customerServiceEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerServiceEntityDTO> partialUpdate(CustomerServiceEntityDTO customerServiceEntityDTO) {
        log.debug("Request to partially update CustomerServiceEntity : {}", customerServiceEntityDTO);

        return customerServiceEntityRepository
            .findById(customerServiceEntityDTO.getId())
            .map(existingCustomerServiceEntity -> {
                customerServiceEntityMapper.partialUpdate(existingCustomerServiceEntity, customerServiceEntityDTO);

                return existingCustomerServiceEntity;
            })
            .map(customerServiceEntityRepository::save)
            .map(customerServiceEntityMapper::toDto);
    }

    /**
     * Get all the customerServiceEntities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerServiceEntityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerServiceEntities");
        return customerServiceEntityRepository.findAll(pageable).map(customerServiceEntityMapper::toDto);
    }

    /**
     *  Get all the customerServiceEntities where CustomerService is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerServiceEntityDTO> findAllWhereCustomerServiceIsNull() {
        log.debug("Request to get all customerServiceEntities where CustomerService is null");
        return StreamSupport
            .stream(customerServiceEntityRepository.findAll().spliterator(), false)
            .filter(customerServiceEntity -> customerServiceEntity.getCustomerService() == null)
            .map(customerServiceEntityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one customerServiceEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerServiceEntityDTO> findOne(UUID id) {
        log.debug("Request to get CustomerServiceEntity : {}", id);
        return customerServiceEntityRepository.findById(id).map(customerServiceEntityMapper::toDto);
    }

    /**
     * Delete the customerServiceEntity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete CustomerServiceEntity : {}", id);
        customerServiceEntityRepository.deleteById(id);
    }
}
