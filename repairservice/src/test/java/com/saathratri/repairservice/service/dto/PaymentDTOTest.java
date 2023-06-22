package com.saathratri.repairservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.repairservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDTO.class);
        PaymentDTO paymentDTO1 = new PaymentDTO();
        paymentDTO1.setId(UUID.randomUUID());
        PaymentDTO paymentDTO2 = new PaymentDTO();
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO2.setId(paymentDTO1.getId());
        assertThat(paymentDTO1).isEqualTo(paymentDTO2);
        paymentDTO2.setId(UUID.randomUUID());
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO1.setId(null);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
    }
}
