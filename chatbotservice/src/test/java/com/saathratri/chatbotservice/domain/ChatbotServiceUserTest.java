package com.saathratri.chatbotservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.chatbotservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ChatbotServiceUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatbotServiceUser.class);
        ChatbotServiceUser chatbotServiceUser1 = new ChatbotServiceUser();
        chatbotServiceUser1.setId(UUID.randomUUID());
        ChatbotServiceUser chatbotServiceUser2 = new ChatbotServiceUser();
        chatbotServiceUser2.setId(chatbotServiceUser1.getId());
        assertThat(chatbotServiceUser1).isEqualTo(chatbotServiceUser2);
        chatbotServiceUser2.setId(UUID.randomUUID());
        assertThat(chatbotServiceUser1).isNotEqualTo(chatbotServiceUser2);
        chatbotServiceUser1.setId(null);
        assertThat(chatbotServiceUser1).isNotEqualTo(chatbotServiceUser2);
    }
}
