package com.saathratri.customerservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.customerservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EndTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(End.class);
        End end1 = new End();
        end1.setId(UUID.randomUUID());
        End end2 = new End();
        end2.setId(end1.getId());
        assertThat(end1).isEqualTo(end2);
        end2.setId(UUID.randomUUID());
        assertThat(end1).isNotEqualTo(end2);
        end1.setId(null);
        assertThat(end1).isNotEqualTo(end2);
    }
}
