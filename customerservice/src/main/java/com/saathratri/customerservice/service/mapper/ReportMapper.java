package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.Report;
import com.saathratri.customerservice.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {}
