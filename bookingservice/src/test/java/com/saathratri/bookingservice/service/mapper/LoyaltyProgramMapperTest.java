package com.saathratri.bookingservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoyaltyProgramMapperTest {

    private LoyaltyProgramMapper loyaltyProgramMapper;

    @BeforeEach
    public void setUp() {
        loyaltyProgramMapper = new LoyaltyProgramMapperImpl();
    }
}
