package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.FAQs;
import com.saathratri.customerservice.repository.FAQsRepository;
import com.saathratri.customerservice.service.dto.FAQsDTO;
import com.saathratri.customerservice.service.mapper.FAQsMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FAQs}.
 */
@Service
@Transactional
public class FAQsService {

    private final Logger log = LoggerFactory.getLogger(FAQsService.class);

    private final FAQsRepository fAQsRepository;

    private final FAQsMapper fAQsMapper;

    public FAQsService(FAQsRepository fAQsRepository, FAQsMapper fAQsMapper) {
        this.fAQsRepository = fAQsRepository;
        this.fAQsMapper = fAQsMapper;
    }

    /**
     * Save a fAQs.
     *
     * @param fAQsDTO the entity to save.
     * @return the persisted entity.
     */
    public FAQsDTO save(FAQsDTO fAQsDTO) {
        log.debug("Request to save FAQs : {}", fAQsDTO);
        FAQs fAQs = fAQsMapper.toEntity(fAQsDTO);
        fAQs = fAQsRepository.save(fAQs);
        return fAQsMapper.toDto(fAQs);
    }

    /**
     * Update a fAQs.
     *
     * @param fAQsDTO the entity to save.
     * @return the persisted entity.
     */
    public FAQsDTO update(FAQsDTO fAQsDTO) {
        log.debug("Request to update FAQs : {}", fAQsDTO);
        FAQs fAQs = fAQsMapper.toEntity(fAQsDTO);
        // no save call needed as we have no fields that can be updated
        return fAQsMapper.toDto(fAQs);
    }

    /**
     * Partially update a fAQs.
     *
     * @param fAQsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FAQsDTO> partialUpdate(FAQsDTO fAQsDTO) {
        log.debug("Request to partially update FAQs : {}", fAQsDTO);

        return fAQsRepository
            .findById(fAQsDTO.getId())
            .map(existingFAQs -> {
                fAQsMapper.partialUpdate(existingFAQs, fAQsDTO);

                return existingFAQs;
            })
            // .map(fAQsRepository::save)
            .map(fAQsMapper::toDto);
    }

    /**
     * Get all the fAQs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FAQsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FAQs");
        return fAQsRepository.findAll(pageable).map(fAQsMapper::toDto);
    }

    /**
     * Get one fAQs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FAQsDTO> findOne(UUID id) {
        log.debug("Request to get FAQs : {}", id);
        return fAQsRepository.findById(id).map(fAQsMapper::toDto);
    }

    /**
     * Delete the fAQs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete FAQs : {}", id);
        fAQsRepository.deleteById(id);
    }
}
