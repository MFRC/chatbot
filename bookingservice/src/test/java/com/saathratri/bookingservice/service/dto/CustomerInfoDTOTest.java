package com.saathratri.bookingservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.bookingservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerInfoDTO.class);
        CustomerInfoDTO customerInfoDTO1 = new CustomerInfoDTO();
        customerInfoDTO1.setId(UUID.randomUUID());
        CustomerInfoDTO customerInfoDTO2 = new CustomerInfoDTO();
        assertThat(customerInfoDTO1).isNotEqualTo(customerInfoDTO2);
        customerInfoDTO2.setId(customerInfoDTO1.getId());
        assertThat(customerInfoDTO1).isEqualTo(customerInfoDTO2);
        customerInfoDTO2.setId(UUID.randomUUID());
        assertThat(customerInfoDTO1).isNotEqualTo(customerInfoDTO2);
        customerInfoDTO1.setId(null);
        assertThat(customerInfoDTO1).isNotEqualTo(customerInfoDTO2);
    }
}
