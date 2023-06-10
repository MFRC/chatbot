package com.saathratri.customerservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.customerservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FAQsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FAQsDTO.class);
        FAQsDTO fAQsDTO1 = new FAQsDTO();
        fAQsDTO1.setId(UUID.randomUUID());
        FAQsDTO fAQsDTO2 = new FAQsDTO();
        assertThat(fAQsDTO1).isNotEqualTo(fAQsDTO2);
        fAQsDTO2.setId(fAQsDTO1.getId());
        assertThat(fAQsDTO1).isEqualTo(fAQsDTO2);
        fAQsDTO2.setId(UUID.randomUUID());
        assertThat(fAQsDTO1).isNotEqualTo(fAQsDTO2);
        fAQsDTO1.setId(null);
        assertThat(fAQsDTO1).isNotEqualTo(fAQsDTO2);
    }
}
