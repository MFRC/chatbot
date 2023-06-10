package com.saathratri.bookingservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.bookingservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LoyaltyProgramDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoyaltyProgramDTO.class);
        LoyaltyProgramDTO loyaltyProgramDTO1 = new LoyaltyProgramDTO();
        loyaltyProgramDTO1.setId(UUID.randomUUID());
        LoyaltyProgramDTO loyaltyProgramDTO2 = new LoyaltyProgramDTO();
        assertThat(loyaltyProgramDTO1).isNotEqualTo(loyaltyProgramDTO2);
        loyaltyProgramDTO2.setId(loyaltyProgramDTO1.getId());
        assertThat(loyaltyProgramDTO1).isEqualTo(loyaltyProgramDTO2);
        loyaltyProgramDTO2.setId(UUID.randomUUID());
        assertThat(loyaltyProgramDTO1).isNotEqualTo(loyaltyProgramDTO2);
        loyaltyProgramDTO1.setId(null);
        assertThat(loyaltyProgramDTO1).isNotEqualTo(loyaltyProgramDTO2);
    }
}
