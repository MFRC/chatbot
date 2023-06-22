package com.saathratri.bookingservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.bookingservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class HotelInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HotelInfo.class);
        HotelInfo hotelInfo1 = new HotelInfo();
        hotelInfo1.setId(UUID.randomUUID());
        HotelInfo hotelInfo2 = new HotelInfo();
        hotelInfo2.setId(hotelInfo1.getId());
        assertThat(hotelInfo1).isEqualTo(hotelInfo2);
        hotelInfo2.setId(UUID.randomUUID());
        assertThat(hotelInfo1).isNotEqualTo(hotelInfo2);
        hotelInfo1.setId(null);
        assertThat(hotelInfo1).isNotEqualTo(hotelInfo2);
    }
}
