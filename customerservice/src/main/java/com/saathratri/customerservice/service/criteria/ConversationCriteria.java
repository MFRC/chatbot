package com.saathratri.customerservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.customerservice.domain.Conversation} entity. This class is used
 * in {@link com.saathratri.customerservice.web.rest.ConversationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conversations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConversationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter question;

    private StringFilter answers;

    private StringFilter reservationNumber;

    private StringFilter phoneNumber;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private StringFilter keyWords;

    private UUIDFilter endId;

    private UUIDFilter faqsId;

    private UUIDFilter customerServiceEntityId;

    private UUIDFilter customerServiceUserId;

    private Boolean distinct;

    public ConversationCriteria() {}

    public ConversationCriteria(ConversationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.question = other.question == null ? null : other.question.copy();
        this.answers = other.answers == null ? null : other.answers.copy();
        this.reservationNumber = other.reservationNumber == null ? null : other.reservationNumber.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.keyWords = other.keyWords == null ? null : other.keyWords.copy();
        this.endId = other.endId == null ? null : other.endId.copy();
        this.faqsId = other.faqsId == null ? null : other.faqsId.copy();
        this.customerServiceEntityId = other.customerServiceEntityId == null ? null : other.customerServiceEntityId.copy();
        this.customerServiceUserId = other.customerServiceUserId == null ? null : other.customerServiceUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConversationCriteria copy() {
        return new ConversationCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getQuestion() {
        return question;
    }

    public StringFilter question() {
        if (question == null) {
            question = new StringFilter();
        }
        return question;
    }

    public void setQuestion(StringFilter question) {
        this.question = question;
    }

    public StringFilter getAnswers() {
        return answers;
    }

    public StringFilter answers() {
        if (answers == null) {
            answers = new StringFilter();
        }
        return answers;
    }

    public void setAnswers(StringFilter answers) {
        this.answers = answers;
    }

    public StringFilter getReservationNumber() {
        return reservationNumber;
    }

    public StringFilter reservationNumber() {
        if (reservationNumber == null) {
            reservationNumber = new StringFilter();
        }
        return reservationNumber;
    }

    public void setReservationNumber(StringFilter reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            startTime = new InstantFilter();
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            endTime = new InstantFilter();
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public StringFilter getKeyWords() {
        return keyWords;
    }

    public StringFilter keyWords() {
        if (keyWords == null) {
            keyWords = new StringFilter();
        }
        return keyWords;
    }

    public void setKeyWords(StringFilter keyWords) {
        this.keyWords = keyWords;
    }

    public UUIDFilter getEndId() {
        return endId;
    }

    public UUIDFilter endId() {
        if (endId == null) {
            endId = new UUIDFilter();
        }
        return endId;
    }

    public void setEndId(UUIDFilter endId) {
        this.endId = endId;
    }

    public UUIDFilter getFaqsId() {
        return faqsId;
    }

    public UUIDFilter faqsId() {
        if (faqsId == null) {
            faqsId = new UUIDFilter();
        }
        return faqsId;
    }

    public void setFaqsId(UUIDFilter faqsId) {
        this.faqsId = faqsId;
    }

    public UUIDFilter getCustomerServiceEntityId() {
        return customerServiceEntityId;
    }

    public UUIDFilter customerServiceEntityId() {
        if (customerServiceEntityId == null) {
            customerServiceEntityId = new UUIDFilter();
        }
        return customerServiceEntityId;
    }

    public void setCustomerServiceEntityId(UUIDFilter customerServiceEntityId) {
        this.customerServiceEntityId = customerServiceEntityId;
    }

    public UUIDFilter getCustomerServiceUserId() {
        return customerServiceUserId;
    }

    public UUIDFilter customerServiceUserId() {
        if (customerServiceUserId == null) {
            customerServiceUserId = new UUIDFilter();
        }
        return customerServiceUserId;
    }

    public void setCustomerServiceUserId(UUIDFilter customerServiceUserId) {
        this.customerServiceUserId = customerServiceUserId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConversationCriteria that = (ConversationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(question, that.question) &&
            Objects.equals(answers, that.answers) &&
            Objects.equals(reservationNumber, that.reservationNumber) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(keyWords, that.keyWords) &&
            Objects.equals(endId, that.endId) &&
            Objects.equals(faqsId, that.faqsId) &&
            Objects.equals(customerServiceEntityId, that.customerServiceEntityId) &&
            Objects.equals(customerServiceUserId, that.customerServiceUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            question,
            answers,
            reservationNumber,
            phoneNumber,
            startTime,
            endTime,
            keyWords,
            endId,
            faqsId,
            customerServiceEntityId,
            customerServiceUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConversationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (question != null ? "question=" + question + ", " : "") +
            (answers != null ? "answers=" + answers + ", " : "") +
            (reservationNumber != null ? "reservationNumber=" + reservationNumber + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (keyWords != null ? "keyWords=" + keyWords + ", " : "") +
            (endId != null ? "endId=" + endId + ", " : "") +
            (faqsId != null ? "faqsId=" + faqsId + ", " : "") +
            (customerServiceEntityId != null ? "customerServiceEntityId=" + customerServiceEntityId + ", " : "") +
            (customerServiceUserId != null ? "customerServiceUserId=" + customerServiceUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
