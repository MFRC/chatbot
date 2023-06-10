package com.saathratri.chatbotservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.chatbotservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ChatSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatSession.class);
        ChatSession chatSession1 = new ChatSession();
        chatSession1.setId(UUID.randomUUID());
        ChatSession chatSession2 = new ChatSession();
        chatSession2.setId(chatSession1.getId());
        assertThat(chatSession1).isEqualTo(chatSession2);
        chatSession2.setId(UUID.randomUUID());
        assertThat(chatSession1).isNotEqualTo(chatSession2);
        chatSession1.setId(null);
        assertThat(chatSession1).isNotEqualTo(chatSession2);
    }
}
