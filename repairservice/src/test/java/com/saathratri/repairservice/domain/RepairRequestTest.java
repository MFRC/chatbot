package com.saathratri.repairservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.repairservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RepairRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepairRequest.class);
        RepairRequest repairRequest1 = new RepairRequest();
        repairRequest1.setId(UUID.randomUUID());
        RepairRequest repairRequest2 = new RepairRequest();
        repairRequest2.setId(repairRequest1.getId());
        assertThat(repairRequest1).isEqualTo(repairRequest2);
        repairRequest2.setId(UUID.randomUUID());
        assertThat(repairRequest1).isNotEqualTo(repairRequest2);
        repairRequest1.setId(null);
        assertThat(repairRequest1).isNotEqualTo(repairRequest2);
    }
}
