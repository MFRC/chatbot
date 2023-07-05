package com.saathratri.customerservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.customerservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerServiceEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerServiceEntity.class);
        CustomerServiceEntity customerServiceEntity1 = new CustomerServiceEntity();
        customerServiceEntity1.setId(UUID.randomUUID());
        CustomerServiceEntity customerServiceEntity2 = new CustomerServiceEntity();
        customerServiceEntity2.setId(customerServiceEntity1.getId());
        assertThat(customerServiceEntity1).isEqualTo(customerServiceEntity2);
        customerServiceEntity2.setId(UUID.randomUUID());
        assertThat(customerServiceEntity1).isNotEqualTo(customerServiceEntity2);
        customerServiceEntity1.setId(null);
        assertThat(customerServiceEntity1).isNotEqualTo(customerServiceEntity2);
    }
}
