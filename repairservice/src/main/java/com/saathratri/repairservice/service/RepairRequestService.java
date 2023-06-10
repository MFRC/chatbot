package com.saathratri.repairservice.service;

import com.saathratri.repairservice.domain.RepairRequest;
import com.saathratri.repairservice.repository.RepairRequestRepository;
import com.saathratri.repairservice.service.dto.RepairRequestDTO;
import com.saathratri.repairservice.service.mapper.RepairRequestMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RepairRequest}.
 */
@Service
@Transactional
public class RepairRequestService {

    private final Logger log = LoggerFactory.getLogger(RepairRequestService.class);

    private final RepairRequestRepository repairRequestRepository;

    private final RepairRequestMapper repairRequestMapper;

    public RepairRequestService(RepairRequestRepository repairRequestRepository, RepairRequestMapper repairRequestMapper) {
        this.repairRequestRepository = repairRequestRepository;
        this.repairRequestMapper = repairRequestMapper;
    }

    /**
     * Save a repairRequest.
     *
     * @param repairRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public RepairRequestDTO save(RepairRequestDTO repairRequestDTO) {
        log.debug("Request to save RepairRequest : {}", repairRequestDTO);
        RepairRequest repairRequest = repairRequestMapper.toEntity(repairRequestDTO);
        repairRequest = repairRequestRepository.save(repairRequest);
        return repairRequestMapper.toDto(repairRequest);
    }

    /**
     * Update a repairRequest.
     *
     * @param repairRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public RepairRequestDTO update(RepairRequestDTO repairRequestDTO) {
        log.debug("Request to update RepairRequest : {}", repairRequestDTO);
        RepairRequest repairRequest = repairRequestMapper.toEntity(repairRequestDTO);
        repairRequest = repairRequestRepository.save(repairRequest);
        return repairRequestMapper.toDto(repairRequest);
    }

    /**
     * Partially update a repairRequest.
     *
     * @param repairRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RepairRequestDTO> partialUpdate(RepairRequestDTO repairRequestDTO) {
        log.debug("Request to partially update RepairRequest : {}", repairRequestDTO);

        return repairRequestRepository
            .findById(repairRequestDTO.getId())
            .map(existingRepairRequest -> {
                repairRequestMapper.partialUpdate(existingRepairRequest, repairRequestDTO);

                return existingRepairRequest;
            })
            .map(repairRequestRepository::save)
            .map(repairRequestMapper::toDto);
    }

    /**
     * Get all the repairRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RepairRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RepairRequests");
        return repairRequestRepository.findAll(pageable).map(repairRequestMapper::toDto);
    }

    /**
     * Get one repairRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RepairRequestDTO> findOne(UUID id) {
        log.debug("Request to get RepairRequest : {}", id);
        return repairRequestRepository.findById(id).map(repairRequestMapper::toDto);
    }

    /**
     * Delete the repairRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete RepairRequest : {}", id);
        repairRequestRepository.deleteById(id);
    }
}
