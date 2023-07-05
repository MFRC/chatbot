package com.saathratri.chatbotservice.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.chatbotservice.domain.ChatSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatSessionDTO implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private Instant startTime;

    private Instant endTime;

    private UserDTO user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
            "id='" + getId() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
