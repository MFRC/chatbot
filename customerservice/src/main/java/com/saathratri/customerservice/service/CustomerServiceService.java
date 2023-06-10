package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.CustomerService;
import com.saathratri.customerservice.repository.CustomerServiceRepository;
import com.saathratri.customerservice.service.dto.CustomerServiceDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerService}.
 */
@Service
@Transactional
public class CustomerServiceService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceService.class);

    private final CustomerServiceRepository customerServiceRepository;

    private final CustomerServiceMapper customerServiceMapper;

    public CustomerServiceService(CustomerServiceRepository customerServiceRepository, CustomerServiceMapper customerServiceMapper) {
        this.customerServiceRepository = customerServiceRepository;
        this.customerServiceMapper = customerServiceMapper;
    }

    /**
     * Save a customerService.
     *
     * @param customerServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerServiceDTO save(CustomerServiceDTO customerServiceDTO) {
        log.debug("Request to save CustomerService : {}", customerServiceDTO);
        CustomerService customerService = customerServiceMapper.toEntity(customerServiceDTO);
        customerService = customerServiceRepository.save(customerService);
        return customerServiceMapper.toDto(customerService);
    }

    /**
     * Update a customerService.
     *
     * @param customerServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerServiceDTO update(CustomerServiceDTO customerServiceDTO) {
        log.debug("Request to update CustomerService : {}", customerServiceDTO);
        CustomerService customerService = customerServiceMapper.toEntity(customerServiceDTO);
        customerService = customerServiceRepository.save(customerService);
        return customerServiceMapper.toDto(customerService);
    }

    /**
     * Partially update a customerService.
     *
     * @param customerServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerServiceDTO> partialUpdate(CustomerServiceDTO customerServiceDTO) {
        log.debug("Request to partially update CustomerService : {}", customerServiceDTO);

        return customerServiceRepository
            .findById(customerServiceDTO.getId())
            .map(existingCustomerService -> {
                customerServiceMapper.partialUpdate(existingCustomerService, customerServiceDTO);

                return existingCustomerService;
            })
            .map(customerServiceRepository::save)
            .map(customerServiceMapper::toDto);
    }

    /**
     * Get all the customerServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerServices");
        return customerServiceRepository.findAll(pageable).map(customerServiceMapper::toDto);
    }

    /**
     * Get one customerService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerServiceDTO> findOne(UUID id) {
        log.debug("Request to get CustomerService : {}", id);
        return customerServiceRepository.findById(id).map(customerServiceMapper::toDto);
    }

    /**
     * Delete the customerService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete CustomerService : {}", id);
        customerServiceRepository.deleteById(id);
    }
}
