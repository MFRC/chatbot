package com.saathratri.chatbotservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saathratri.chatbotservice.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = new Message();
        message1.setId(UUID.randomUUID());
        Message message2 = new Message();
        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);
        message2.setId(UUID.randomUUID());
        assertThat(message1).isNotEqualTo(message2);
        message1.setId(null);
        assertThat(message1).isNotEqualTo(message2);
    }
}
