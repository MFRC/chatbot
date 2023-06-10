package com.saathratri.bookingservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HotelInfoMapperTest {

    private HotelInfoMapper hotelInfoMapper;

    @BeforeEach
    public void setUp() {
        hotelInfoMapper = new HotelInfoMapperImpl();
    }
}
