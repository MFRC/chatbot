package com.saathratri.bookingservice.service.mapper;

import com.saathratri.bookingservice.domain.LoyaltyProgram;
import com.saathratri.bookingservice.service.dto.LoyaltyProgramDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoyaltyProgram} and its DTO {@link LoyaltyProgramDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoyaltyProgramMapper extends EntityMapper<LoyaltyProgramDTO, LoyaltyProgram> {}
