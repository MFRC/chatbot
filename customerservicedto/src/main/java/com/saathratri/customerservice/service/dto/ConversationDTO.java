package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.Conversation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConversationDTO implements Serializable {

  private Long id;

  private String question;

  private String answers;

  private String reservationNumber;

  private String phoneNumber;

  private ZonedDateTime startTime;

  private ZonedDateTime endTime;

  private String keyWords;

  private EndDTO end;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswers() {
    return answers;
  }

  public void setAnswers(String answers) {
    this.answers = answers;
  }

  public String getReservationNumber() {
    return reservationNumber;
  }

  public void setReservationNumber(String reservationNumber) {
    this.reservationNumber = reservationNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
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

  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  public EndDTO getEnd() {
    return end;
  }

  public void setEnd(EndDTO end) {
    this.end = end;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ConversationDTO)) {
      return false;
    }

    ConversationDTO conversationDTO = (ConversationDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, conversationDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "ConversationDTO{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", answers='" + getAnswers() + "'" +
            ", reservationNumber='" + getReservationNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", keyWords='" + getKeyWords() + "'" +
            ", end=" + getEnd() +
            "}";
    }
}
