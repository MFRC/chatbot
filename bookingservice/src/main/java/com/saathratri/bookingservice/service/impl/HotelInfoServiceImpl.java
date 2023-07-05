package com.saathratri.bookingservice.service.impl;

import com.saathratri.bookingservice.domain.HotelInfo;
import com.saathratri.bookingservice.repository.HotelInfoRepository;
import com.saathratri.bookingservice.service.HotelInfoService;
import com.saathratri.bookingservice.service.dto.HotelInfoDTO;
import com.saathratri.bookingservice.service.mapper.HotelInfoMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HotelInfo}.
 */
@Service
@Transactional
public class HotelInfoServiceImpl implements HotelInfoService {

    private final Logger log = LoggerFactory.getLogger(HotelInfoServiceImpl.class);

    private final HotelInfoRepository hotelInfoRepository;

    private final HotelInfoMapper hotelInfoMapper;

    public HotelInfoServiceImpl(HotelInfoRepository hotelInfoRepository, HotelInfoMapper hotelInfoMapper) {
        this.hotelInfoRepository = hotelInfoRepository;
        this.hotelInfoMapper = hotelInfoMapper;
    }

    @Override
    public HotelInfoDTO save(HotelInfoDTO hotelInfoDTO) {
        log.debug("Request to save HotelInfo : {}", hotelInfoDTO);
        HotelInfo hotelInfo = hotelInfoMapper.toEntity(hotelInfoDTO);
        hotelInfo = hotelInfoRepository.save(hotelInfo);
        return hotelInfoMapper.toDto(hotelInfo);
    }

    @Override
    public HotelInfoDTO update(HotelInfoDTO hotelInfoDTO) {
        log.debug("Request to update HotelInfo : {}", hotelInfoDTO);
        HotelInfo hotelInfo = hotelInfoMapper.toEntity(hotelInfoDTO);
        hotelInfo = hotelInfoRepository.save(hotelInfo);
        return hotelInfoMapper.toDto(hotelInfo);
    }

    @Override
    public Optional<HotelInfoDTO> partialUpdate(HotelInfoDTO hotelInfoDTO) {
        log.debug("Request to partially update HotelInfo : {}", hotelInfoDTO);

        return hotelInfoRepository
            .findById(hotelInfoDTO.getId())
            .map(existingHotelInfo -> {
                hotelInfoMapper.partialUpdate(existingHotelInfo, hotelInfoDTO);

                return existingHotelInfo;
            })
            .map(hotelInfoRepository::save)
            .map(hotelInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HotelInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HotelInfos");
        return hotelInfoRepository.findAll(pageable).map(hotelInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HotelInfoDTO> findOne(UUID id) {
        log.debug("Request to get HotelInfo : {}", id);
        return hotelInfoRepository.findById(id).map(hotelInfoMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete HotelInfo : {}", id);
        hotelInfoRepository.deleteById(id);
    }
}
