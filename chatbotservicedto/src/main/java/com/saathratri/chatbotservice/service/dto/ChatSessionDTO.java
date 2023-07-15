package com.saathratri.chatbotservice.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.chatbotservice.domain.ChatSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatSessionDTO implements Serializable {

  private Long id;

  @NotNull
  private ZonedDateTime startTime;

  private ZonedDateTime endTime;

  private ChatbotServiceUserDTO chatbotServiceUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ZonedDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(ZonedDateTime startTime) {
    this.startTime = startTime;
  }

  public ZonedDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(ZonedDateTime endTime) {
    this.endTime = endTime;
  }

  public ChatbotServiceUserDTO getChatbotServiceUser() {
    return chatbotServiceUser;
  }

  public void setChatbotServiceUser(ChatbotServiceUserDTO chatbotServiceUser) {
    this.chatbotServiceUser = chatbotServiceUser;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ChatSessionDTO)) {
      return false;
    }

    ChatSessionDTO chatSessionDTO = (ChatSessionDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, chatSessionDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "ChatSessionDTO{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", chatbotServiceUser=" + getChatbotServiceUser() +
            "}";
    }
}
