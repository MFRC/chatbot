package com.saathratri.chatbotservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.chatbotservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ChatSessionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatSessionDTO.class);
        ChatSessionDTO chatSessionDTO1 = new ChatSessionDTO();
        chatSessionDTO1.setId(UUID.randomUUID());
        ChatSessionDTO chatSessionDTO2 = new ChatSessionDTO();
        assertThat(chatSessionDTO1).isNotEqualTo(chatSessionDTO2);
        chatSessionDTO2.setId(chatSessionDTO1.getId());
        assertThat(chatSessionDTO1).isEqualTo(chatSessionDTO2);
        chatSessionDTO2.setId(UUID.randomUUID());
        assertThat(chatSessionDTO1).isNotEqualTo(chatSessionDTO2);
        chatSessionDTO1.setId(null);
        assertThat(chatSessionDTO1).isNotEqualTo(chatSessionDTO2);
    }
}
