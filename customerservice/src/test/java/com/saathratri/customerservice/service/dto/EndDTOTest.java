package com.saathratri.customerservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.customerservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EndDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EndDTO.class);
        EndDTO endDTO1 = new EndDTO();
        endDTO1.setId(UUID.randomUUID());
        EndDTO endDTO2 = new EndDTO();
        assertThat(endDTO1).isNotEqualTo(endDTO2);
        endDTO2.setId(endDTO1.getId());
        assertThat(endDTO1).isEqualTo(endDTO2);
        endDTO2.setId(UUID.randomUUID());
        assertThat(endDTO1).isNotEqualTo(endDTO2);
        endDTO1.setId(null);
        assertThat(endDTO1).isNotEqualTo(endDTO2);
    }
}
