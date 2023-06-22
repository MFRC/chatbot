package com.saathratri.bookingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.bookingservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerInfo.class);
        CustomerInfo customerInfo1 = new CustomerInfo();
        customerInfo1.setId(UUID.randomUUID());
        CustomerInfo customerInfo2 = new CustomerInfo();
        customerInfo2.setId(customerInfo1.getId());
        assertThat(customerInfo1).isEqualTo(customerInfo2);
        customerInfo2.setId(UUID.randomUUID());
        assertThat(customerInfo1).isNotEqualTo(customerInfo2);
        customerInfo1.setId(null);
        assertThat(customerInfo1).isNotEqualTo(customerInfo2);
    }
}
