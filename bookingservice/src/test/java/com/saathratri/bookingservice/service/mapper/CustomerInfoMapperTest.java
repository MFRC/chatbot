package com.saathratri.bookingservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerInfoMapperTest {

    private CustomerInfoMapper customerInfoMapper;

    @BeforeEach
    public void setUp() {
        customerInfoMapper = new CustomerInfoMapperImpl();
    }
}
