package com.saathratri.chatbotservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.chatbotservice.domain.ChatbotServiceUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatbotServiceUserDTO implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
