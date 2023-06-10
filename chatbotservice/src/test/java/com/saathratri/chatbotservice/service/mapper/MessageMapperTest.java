package com.saathratri.chatbotservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageMapperTest {

    private MessageMapper messageMapper;

    @BeforeEach
    public void setUp() {
        messageMapper = new MessageMapperImpl();
    }
}
