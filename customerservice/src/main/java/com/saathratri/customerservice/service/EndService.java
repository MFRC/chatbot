package com.saathratri.customerservice.service;

import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.repository.EndRepository;
import com.saathratri.customerservice.service.dto.EndDTO;
import com.saathratri.customerservice.service.mapper.EndMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link End}.
 */
@Service
@Transactional
public class EndService {

    private final Logger log = LoggerFactory.getLogger(EndService.class);

    private final EndRepository endRepository;

    private final EndMapper endMapper;

    public EndService(EndRepository endRepository, EndMapper endMapper) {
        this.endRepository = endRepository;
        this.endMapper = endMapper;
    }

    /**
     * Save a end.
     *
     * @param endDTO the entity to save.
     * @return the persisted entity.
     */
    public EndDTO save(EndDTO endDTO) {
        log.debug("Request to save End : {}", endDTO);
        End end = endMapper.toEntity(endDTO);
        end = endRepository.save(end);
        return endMapper.toDto(end);
    }

    /**
     * Update a end.
     *
     * @param endDTO the entity to save.
     * @return the persisted entity.
     */
    public EndDTO update(EndDTO endDTO) {
        log.debug("Request to update End : {}", endDTO);
        End end = endMapper.toEntity(endDTO);
        // no save call needed as we have no fields that can be updated
        return endMapper.toDto(end);
    }

    /**
     * Partially update a end.
     *
     * @param endDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EndDTO> partialUpdate(EndDTO endDTO) {
        log.debug("Request to partially update End : {}", endDTO);

        return endRepository
            .findById(endDTO.getId())
            .map(existingEnd -> {
                endMapper.partialUpdate(existingEnd, endDTO);

                return existingEnd;
            })
            // .map(endRepository::save)
            .map(endMapper::toDto);
    }

    /**
     * Get all the ends.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EndDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ends");
        return endRepository.findAll(pageable).map(endMapper::toDto);
    }

    /**
     * Get one end by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EndDTO> findOne(UUID id) {
        log.debug("Request to get End : {}", id);
        return endRepository.findById(id).map(endMapper::toDto);
    }

    /**
     * Delete the end by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete End : {}", id);
        endRepository.deleteById(id);
    }
}
