package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.Faq} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FaqDTO implements Serializable {

  private Long id;

  private String answers;

  private String question;

  private String keyWords;

  private ConversationDTO conversation;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
    if (!(o instanceof FaqDTO)) {
      return false;
    }

    FaqDTO faqDTO = (FaqDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, faqDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "FaqDTO{" +
            "id=" + getId() +
            ", answers='" + getAnswers() + "'" +
            ", question='" + getQuestion() + "'" +
            ", keyWords='" + getKeyWords() + "'" +
            ", conversation=" + getConversation() +
            "}";
    }
}
