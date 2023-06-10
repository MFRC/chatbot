package com.saathratri.repairservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepairRequestMapperTest {

    private RepairRequestMapper repairRequestMapper;

    @BeforeEach
    public void setUp() {
        repairRequestMapper = new RepairRequestMapperImpl();
    }
}
