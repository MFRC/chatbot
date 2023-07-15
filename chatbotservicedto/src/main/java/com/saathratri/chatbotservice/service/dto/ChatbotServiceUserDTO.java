package com.saathratri.chatbotservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.chatbotservice.domain.ChatbotServiceUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatbotServiceUserDTO implements Serializable {

  private Long id;

  @NotNull
  private String name;

  private String email;

  private String phoneNumber;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ChatbotServiceUserDTO)) {
      return false;
    }

    ChatbotServiceUserDTO chatbotServiceUserDTO = (ChatbotServiceUserDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, chatbotServiceUserDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "ChatbotServiceUserDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
