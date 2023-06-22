package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.FAQs} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FAQsDTO implements Serializable {

    @NotNull
    private UUID id;

    private String answers;

    private String question;

    private String keyWords;

    private ConversationDTO conversation;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public ConversationDTO getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDTO conversation) {
        this.conversation = conversation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FAQsDTO)) {
            return false;
        }

        FAQsDTO fAQsDTO = (FAQsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fAQsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FAQsDTO{" +
            "id='" + getId() + "'" +
            ", answers='" + getAnswers() + "'" +
            ", question='" + getQuestion() + "'" +
            ", keyWords='" + getKeyWords() + "'" +
            ", conversation=" + getConversation() +
            "}";
    }
}
