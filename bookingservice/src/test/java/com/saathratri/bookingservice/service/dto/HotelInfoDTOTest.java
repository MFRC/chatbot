package com.saathratri.bookingservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.bookingservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class HotelInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HotelInfoDTO.class);
        HotelInfoDTO hotelInfoDTO1 = new HotelInfoDTO();
        hotelInfoDTO1.setId(UUID.randomUUID());
        HotelInfoDTO hotelInfoDTO2 = new HotelInfoDTO();
        assertThat(hotelInfoDTO1).isNotEqualTo(hotelInfoDTO2);
        hotelInfoDTO2.setId(hotelInfoDTO1.getId());
        assertThat(hotelInfoDTO1).isEqualTo(hotelInfoDTO2);
        hotelInfoDTO2.setId(UUID.randomUUID());
        assertThat(hotelInfoDTO1).isNotEqualTo(hotelInfoDTO2);
        hotelInfoDTO1.setId(null);
        assertThat(hotelInfoDTO1).isNotEqualTo(hotelInfoDTO2);
    }
}
