package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.CustomerServiceUser;
import com.saathratri.customerservice.repository.CustomerServiceUserRepository;
import com.saathratri.customerservice.service.dto.CustomerServiceUserDTO;
import com.saathratri.customerservice.service.mapper.CustomerServiceUserMapper;
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
 * Service Implementation for managing {@link CustomerServiceUser}.
 */
@Service
@Transactional
public class CustomerServiceUserService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceUserService.class);

    private final CustomerServiceUserRepository customerServiceUserRepository;

    private final CustomerServiceUserMapper customerServiceUserMapper;

    public CustomerServiceUserService(
        CustomerServiceUserRepository customerServiceUserRepository,
        CustomerServiceUserMapper customerServiceUserMapper
    ) {
        this.customerServiceUserRepository = customerServiceUserRepository;
        this.customerServiceUserMapper = customerServiceUserMapper;
    }

    /**
     * Save a customerServiceUser.
     *
     * @param customerServiceUserDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerServiceUserDTO save(CustomerServiceUserDTO customerServiceUserDTO) {
        log.debug("Request to save CustomerServiceUser : {}", customerServiceUserDTO);
        CustomerServiceUser customerServiceUser = customerServiceUserMapper.toEntity(customerServiceUserDTO);
        customerServiceUser = customerServiceUserRepository.save(customerServiceUser);
        return customerServiceUserMapper.toDto(customerServiceUser);
    }

    /**
     * Update a customerServiceUser.
     *
     * @param customerServiceUserDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerServiceUserDTO update(CustomerServiceUserDTO customerServiceUserDTO) {
        log.debug("Request to update CustomerServiceUser : {}", customerServiceUserDTO);
        CustomerServiceUser customerServiceUser = customerServiceUserMapper.toEntity(customerServiceUserDTO);
        customerServiceUser = customerServiceUserRepository.save(customerServiceUser);
        return customerServiceUserMapper.toDto(customerServiceUser);
    }

    /**
     * Partially update a customerServiceUser.
     *
     * @param customerServiceUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerServiceUserDTO> partialUpdate(CustomerServiceUserDTO customerServiceUserDTO) {
        log.debug("Request to partially update CustomerServiceUser : {}", customerServiceUserDTO);

        return customerServiceUserRepository
            .findById(customerServiceUserDTO.getId())
            .map(existingCustomerServiceUser -> {
                customerServiceUserMapper.partialUpdate(existingCustomerServiceUser, customerServiceUserDTO);

                return existingCustomerServiceUser;
            })
            .map(customerServiceUserRepository::save)
            .map(customerServiceUserMapper::toDto);
    }

    /**
     * Get all the customerServiceUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerServiceUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerServiceUsers");
        return customerServiceUserRepository.findAll(pageable).map(customerServiceUserMapper::toDto);
    }

    /**
     *  Get all the customerServiceUsers where CustomerService is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerServiceUserDTO> findAllWhereCustomerServiceIsNull() {
        log.debug("Request to get all customerServiceUsers where CustomerService is null");
        return StreamSupport
            .stream(customerServiceUserRepository.findAll().spliterator(), false)
            .filter(customerServiceUser -> customerServiceUser.getCustomerService() == null)
            .map(customerServiceUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one customerServiceUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerServiceUserDTO> findOne(UUID id) {
        log.debug("Request to get CustomerServiceUser : {}", id);
        return customerServiceUserRepository.findById(id).map(customerServiceUserMapper::toDto);
    }

    /**
     * Delete the customerServiceUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete CustomerServiceUser : {}", id);
        customerServiceUserRepository.deleteById(id);
    }
}
