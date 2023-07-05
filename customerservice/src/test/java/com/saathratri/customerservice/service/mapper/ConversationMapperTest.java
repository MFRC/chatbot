package com.saathratri.customerservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConversationMapperTest {

    private ConversationMapper conversationMapper;

    @BeforeEach
    public void setUp() {
        conversationMapper = new ConversationMapperImpl();
    }
}
