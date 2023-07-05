package com.saathratri.chatbotservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatSessionMapperTest {

    private ChatSessionMapper chatSessionMapper;

    @BeforeEach
    public void setUp() {
        chatSessionMapper = new ChatSessionMapperImpl();
    }
}
