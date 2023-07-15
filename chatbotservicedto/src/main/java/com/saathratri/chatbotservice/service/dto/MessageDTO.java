package com.saathratri.chatbotservice.service.dto;

import com.saathratri.chatbotservice.domain.enumeration.SenderType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.chatbotservice.domain.Message} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageDTO implements Serializable {

  private Long id;

  @NotNull
  private String content;

  @NotNull
  private ZonedDateTime timestamp;

  @NotNull
  private SenderType senderType;

  private ChatbotServiceUserDTO chatbotServiceUser;

  private ChatSessionDTO chatSession;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(ZonedDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public SenderType getSenderType() {
    return senderType;
  }

  public void setSenderType(SenderType senderType) {
    this.senderType = senderType;
  }

  public ChatbotServiceUserDTO getChatbotServiceUser() {
    return chatbotServiceUser;
  }

  public void setChatbotServiceUser(ChatbotServiceUserDTO chatbotServiceUser) {
    this.chatbotServiceUser = chatbotServiceUser;
  }

  public ChatSessionDTO getChatSession() {
    return chatSession;
  }

  public void setChatSession(ChatSessionDTO chatSession) {
    this.chatSession = chatSession;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MessageDTO)) {
      return false;
    }

    MessageDTO messageDTO = (MessageDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, messageDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", senderType='" + getSenderType() + "'" +
            ", chatbotServiceUser=" + getChatbotServiceUser() +
            ", chatSession=" + getChatSession() +
            "}";
    }
}
