package com.saathratri.bookingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.bookingservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LoyaltyProgramTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoyaltyProgram.class);
        LoyaltyProgram loyaltyProgram1 = new LoyaltyProgram();
        loyaltyProgram1.setId(UUID.randomUUID());
        LoyaltyProgram loyaltyProgram2 = new LoyaltyProgram();
        loyaltyProgram2.setId(loyaltyProgram1.getId());
        assertThat(loyaltyProgram1).isEqualTo(loyaltyProgram2);
        loyaltyProgram2.setId(UUID.randomUUID());
        assertThat(loyaltyProgram1).isNotEqualTo(loyaltyProgram2);
        loyaltyProgram1.setId(null);
        assertThat(loyaltyProgram1).isNotEqualTo(loyaltyProgram2);
    }
}
