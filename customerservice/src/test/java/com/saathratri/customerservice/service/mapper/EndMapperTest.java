package com.saathratri.customerservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EndMapperTest {

    private EndMapper endMapper;

    @BeforeEach
    public void setUp() {
        endMapper = new EndMapperImpl();
    }
}
