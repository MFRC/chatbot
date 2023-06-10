package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.FAQs;
import com.saathratri.customerservice.service.dto.FAQsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FAQs} and its DTO {@link FAQsDTO}.
 */
@Mapper(componentModel = "spring")
public interface FAQsMapper extends EntityMapper<FAQsDTO, FAQs> {}
