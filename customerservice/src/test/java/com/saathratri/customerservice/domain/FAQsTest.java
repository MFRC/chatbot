package com.saathratri.customerservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.customerservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FAQsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FAQs.class);
        FAQs fAQs1 = new FAQs();
        fAQs1.setId(UUID.randomUUID());
        FAQs fAQs2 = new FAQs();
        fAQs2.setId(fAQs1.getId());
        assertThat(fAQs1).isEqualTo(fAQs2);
        fAQs2.setId(UUID.randomUUID());
        assertThat(fAQs1).isNotEqualTo(fAQs2);
        fAQs1.setId(null);
        assertThat(fAQs1).isNotEqualTo(fAQs2);
    }
}
