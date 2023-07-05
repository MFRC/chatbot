package com.saathratri.customerservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.customerservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerServiceUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerServiceUser.class);
        CustomerServiceUser customerServiceUser1 = new CustomerServiceUser();
        customerServiceUser1.setId(UUID.randomUUID());
        CustomerServiceUser customerServiceUser2 = new CustomerServiceUser();
        customerServiceUser2.setId(customerServiceUser1.getId());
        assertThat(customerServiceUser1).isEqualTo(customerServiceUser2);
        customerServiceUser2.setId(UUID.randomUUID());
        assertThat(customerServiceUser1).isNotEqualTo(customerServiceUser2);
        customerServiceUser1.setId(null);
        assertThat(customerServiceUser1).isNotEqualTo(customerServiceUser2);
    }
}
