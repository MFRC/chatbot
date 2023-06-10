package com.saathratri.customerservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.customerservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerServiceUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerServiceUserDTO.class);
        CustomerServiceUserDTO customerServiceUserDTO1 = new CustomerServiceUserDTO();
        customerServiceUserDTO1.setId(UUID.randomUUID());
        CustomerServiceUserDTO customerServiceUserDTO2 = new CustomerServiceUserDTO();
        assertThat(customerServiceUserDTO1).isNotEqualTo(customerServiceUserDTO2);
        customerServiceUserDTO2.setId(customerServiceUserDTO1.getId());
        assertThat(customerServiceUserDTO1).isEqualTo(customerServiceUserDTO2);
        customerServiceUserDTO2.setId(UUID.randomUUID());
        assertThat(customerServiceUserDTO1).isNotEqualTo(customerServiceUserDTO2);
        customerServiceUserDTO1.setId(null);
        assertThat(customerServiceUserDTO1).isNotEqualTo(customerServiceUserDTO2);
    }
}
