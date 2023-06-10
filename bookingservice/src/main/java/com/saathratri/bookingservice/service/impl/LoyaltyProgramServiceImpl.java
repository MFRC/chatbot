package com.saathratri.bookingservice.service.impl;

import com.saathratri.bookingservice.domain.LoyaltyProgram;
import com.saathratri.bookingservice.repository.LoyaltyProgramRepository;
import com.saathratri.bookingservice.service.LoyaltyProgramService;
import com.saathratri.bookingservice.service.dto.LoyaltyProgramDTO;
import com.saathratri.bookingservice.service.mapper.LoyaltyProgramMapper;
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
 * Service Implementation for managing {@link LoyaltyProgram}.
 */
@Service
@Transactional
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {

    private final Logger log = LoggerFactory.getLogger(LoyaltyProgramServiceImpl.class);

    private final LoyaltyProgramRepository loyaltyProgramRepository;

    private final LoyaltyProgramMapper loyaltyProgramMapper;

    public LoyaltyProgramServiceImpl(LoyaltyProgramRepository loyaltyProgramRepository, LoyaltyProgramMapper loyaltyProgramMapper) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
        this.loyaltyProgramMapper = loyaltyProgramMapper;
    }

    @Override
    public LoyaltyProgramDTO save(LoyaltyProgramDTO loyaltyProgramDTO) {
        log.debug("Request to save LoyaltyProgram : {}", loyaltyProgramDTO);
        LoyaltyProgram loyaltyProgram = loyaltyProgramMapper.toEntity(loyaltyProgramDTO);
        loyaltyProgram = loyaltyProgramRepository.save(loyaltyProgram);
        return loyaltyProgramMapper.toDto(loyaltyProgram);
    }

    @Override
    public LoyaltyProgramDTO update(LoyaltyProgramDTO loyaltyProgramDTO) {
        log.debug("Request to update LoyaltyProgram : {}", loyaltyProgramDTO);
        LoyaltyProgram loyaltyProgram = loyaltyProgramMapper.toEntity(loyaltyProgramDTO);
        loyaltyProgram = loyaltyProgramRepository.save(loyaltyProgram);
        return loyaltyProgramMapper.toDto(loyaltyProgram);
    }

    @Override
    public Optional<LoyaltyProgramDTO> partialUpdate(LoyaltyProgramDTO loyaltyProgramDTO) {
        log.debug("Request to partially update LoyaltyProgram : {}", loyaltyProgramDTO);

        return loyaltyProgramRepository
            .findById(loyaltyProgramDTO.getId())
            .map(existingLoyaltyProgram -> {
                loyaltyProgramMapper.partialUpdate(existingLoyaltyProgram, loyaltyProgramDTO);

                return existingLoyaltyProgram;
            })
            .map(loyaltyProgramRepository::save)
            .map(loyaltyProgramMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoyaltyProgramDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoyaltyPrograms");
        return loyaltyProgramRepository.findAll(pageable).map(loyaltyProgramMapper::toDto);
    }

    /**
     *  Get all the loyaltyPrograms where Hotel is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LoyaltyProgramDTO> findAllWhereHotelIsNull() {
        log.debug("Request to get all loyaltyPrograms where Hotel is null");
        return StreamSupport
            .stream(loyaltyProgramRepository.findAll().spliterator(), false)
            .filter(loyaltyProgram -> loyaltyProgram.getHotel() == null)
            .map(loyaltyProgramMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoyaltyProgramDTO> findOne(UUID id) {
        log.debug("Request to get LoyaltyProgram : {}", id);
        return loyaltyProgramRepository.findById(id).map(loyaltyProgramMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete LoyaltyProgram : {}", id);
        loyaltyProgramRepository.deleteById(id);
    }
}
