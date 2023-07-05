package com.saathratri.bookingservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.bookingservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReservationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationDTO.class);
        ReservationDTO reservationDTO1 = new ReservationDTO();
        reservationDTO1.setId(UUID.randomUUID());
        ReservationDTO reservationDTO2 = new ReservationDTO();
        assertThat(reservationDTO1).isNotEqualTo(reservationDTO2);
        reservationDTO2.setId(reservationDTO1.getId());
        assertThat(reservationDTO1).isEqualTo(reservationDTO2);
        reservationDTO2.setId(UUID.randomUUID());
        assertThat(reservationDTO1).isNotEqualTo(reservationDTO2);
        reservationDTO1.setId(null);
        assertThat(reservationDTO1).isNotEqualTo(reservationDTO2);
    }
}
