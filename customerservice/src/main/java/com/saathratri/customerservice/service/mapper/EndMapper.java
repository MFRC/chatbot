package com.saathratri.customerservice.service.mapper;

import com.saathratri.customerservice.domain.End;
import com.saathratri.customerservice.domain.Report;
import com.saathratri.customerservice.service.dto.EndDTO;
import com.saathratri.customerservice.service.dto.ReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link End} and its DTO {@link EndDTO}.
 */
@Mapper(componentModel = "spring")
public interface EndMapper extends EntityMapper<EndDTO, End> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    EndDTO toDto(End s);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
