package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.service.dto.EndDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link End} and its DTO {@link EndDTO}.
 */
@Mapper(componentModel = "spring")
public interface EndMapper extends EntityMapper<EndDTO, End> {}
