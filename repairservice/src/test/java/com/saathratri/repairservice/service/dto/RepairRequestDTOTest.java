package com.saathratri.repairservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.repairservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RepairRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepairRequestDTO.class);
        RepairRequestDTO repairRequestDTO1 = new RepairRequestDTO();
        repairRequestDTO1.setId(UUID.randomUUID());
        RepairRequestDTO repairRequestDTO2 = new RepairRequestDTO();
        assertThat(repairRequestDTO1).isNotEqualTo(repairRequestDTO2);
        repairRequestDTO2.setId(repairRequestDTO1.getId());
        assertThat(repairRequestDTO1).isEqualTo(repairRequestDTO2);
        repairRequestDTO2.setId(UUID.randomUUID());
        assertThat(repairRequestDTO1).isNotEqualTo(repairRequestDTO2);
        repairRequestDTO1.setId(null);
        assertThat(repairRequestDTO1).isNotEqualTo(repairRequestDTO2);
    }
}
