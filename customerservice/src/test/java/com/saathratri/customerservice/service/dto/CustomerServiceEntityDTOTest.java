package com.saathratri.customerservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.customerservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerServiceEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerServiceEntityDTO.class);
        CustomerServiceEntityDTO customerServiceEntityDTO1 = new CustomerServiceEntityDTO();
        customerServiceEntityDTO1.setId(UUID.randomUUID());
        CustomerServiceEntityDTO customerServiceEntityDTO2 = new CustomerServiceEntityDTO();
        assertThat(customerServiceEntityDTO1).isNotEqualTo(customerServiceEntityDTO2);
        customerServiceEntityDTO2.setId(customerServiceEntityDTO1.getId());
        assertThat(customerServiceEntityDTO1).isEqualTo(customerServiceEntityDTO2);
        customerServiceEntityDTO2.setId(UUID.randomUUID());
        assertThat(customerServiceEntityDTO1).isNotEqualTo(customerServiceEntityDTO2);
        customerServiceEntityDTO1.setId(null);
        assertThat(customerServiceEntityDTO1).isNotEqualTo(customerServiceEntityDTO2);
    }
}
