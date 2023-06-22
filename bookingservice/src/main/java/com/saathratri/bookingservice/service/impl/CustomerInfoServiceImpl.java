package com.saathratri.bookingservice.service.impl;

import com.saathratri.bookingservice.domain.CustomerInfo;
import com.saathratri.bookingservice.repository.CustomerInfoRepository;
import com.saathratri.bookingservice.service.CustomerInfoService;
import com.saathratri.bookingservice.service.dto.CustomerInfoDTO;
import com.saathratri.bookingservice.service.mapper.CustomerInfoMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerInfo}.
 */
@Service
@Transactional
public class CustomerInfoServiceImpl implements CustomerInfoService {

    private final Logger log = LoggerFactory.getLogger(CustomerInfoServiceImpl.class);

    private final CustomerInfoRepository customerInfoRepository;

    private final CustomerInfoMapper customerInfoMapper;

    public CustomerInfoServiceImpl(CustomerInfoRepository customerInfoRepository, CustomerInfoMapper customerInfoMapper) {
        this.customerInfoRepository = customerInfoRepository;
        this.customerInfoMapper = customerInfoMapper;
    }

    @Override
    public CustomerInfoDTO save(CustomerInfoDTO customerInfoDTO) {
        log.debug("Request to save CustomerInfo : {}", customerInfoDTO);
        CustomerInfo customerInfo = customerInfoMapper.toEntity(customerInfoDTO);
        customerInfo = customerInfoRepository.save(customerInfo);
        return customerInfoMapper.toDto(customerInfo);
    }

    @Override
    public CustomerInfoDTO update(CustomerInfoDTO customerInfoDTO) {
        log.debug("Request to update CustomerInfo : {}", customerInfoDTO);
        CustomerInfo customerInfo = customerInfoMapper.toEntity(customerInfoDTO);
        customerInfo = customerInfoRepository.save(customerInfo);
        return customerInfoMapper.toDto(customerInfo);
    }

    @Override
    public Optional<CustomerInfoDTO> partialUpdate(CustomerInfoDTO customerInfoDTO) {
        log.debug("Request to partially update CustomerInfo : {}", customerInfoDTO);

        return customerInfoRepository
            .findById(customerInfoDTO.getId())
            .map(existingCustomerInfo -> {
                customerInfoMapper.partialUpdate(existingCustomerInfo, customerInfoDTO);

                return existingCustomerInfo;
            })
            .map(customerInfoRepository::save)
            .map(customerInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerInfos");
        return customerInfoRepository.findAll(pageable).map(customerInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerInfoDTO> findOne(UUID id) {
        log.debug("Request to get CustomerInfo : {}", id);
        return customerInfoRepository.findById(id).map(customerInfoMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CustomerInfo : {}", id);
        customerInfoRepository.deleteById(id);
    }
}
