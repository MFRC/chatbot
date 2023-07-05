package com.saathratri.chatbotservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.chatbotservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ChatbotServiceUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatbotServiceUserDTO.class);
        ChatbotServiceUserDTO chatbotServiceUserDTO1 = new ChatbotServiceUserDTO();
        chatbotServiceUserDTO1.setId(UUID.randomUUID());
        ChatbotServiceUserDTO chatbotServiceUserDTO2 = new ChatbotServiceUserDTO();
        assertThat(chatbotServiceUserDTO1).isNotEqualTo(chatbotServiceUserDTO2);
        chatbotServiceUserDTO2.setId(chatbotServiceUserDTO1.getId());
        assertThat(chatbotServiceUserDTO1).isEqualTo(chatbotServiceUserDTO2);
        chatbotServiceUserDTO2.setId(UUID.randomUUID());
        assertThat(chatbotServiceUserDTO1).isNotEqualTo(chatbotServiceUserDTO2);
        chatbotServiceUserDTO1.setId(null);
        assertThat(chatbotServiceUserDTO1).isNotEqualTo(chatbotServiceUserDTO2);
    }
}
