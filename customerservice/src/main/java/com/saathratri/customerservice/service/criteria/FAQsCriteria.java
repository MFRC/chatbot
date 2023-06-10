package com.saathratri.customerservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.customerservice.domain.FAQs} entity. This class is used
 * in {@link com.saathratri.customerservice.web.rest.FAQsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fa-qs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FAQsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter answers;

    private StringFilter question;

    private StringFilter keyWords;

    private UUIDFilter conversationId;

    private UUIDFilter customerServiceId;

    private Boolean distinct;

    public FAQsCriteria() {}

    public FAQsCriteria(FAQsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.answers = other.answers == null ? null : other.answers.copy();
        this.question = other.question == null ? null : other.question.copy();
        this.keyWords = other.keyWords == null ? null : other.keyWords.copy();
        this.conversationId = other.conversationId == null ? null : other.conversationId.copy();
        this.customerServiceId = other.customerServiceId == null ? null : other.customerServiceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FAQsCriteria copy() {
        return new FAQsCriteria(this);
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

    public UUIDFilter getConversationId() {
        return conversationId;
    }

    public UUIDFilter conversationId() {
        if (conversationId == null) {
            conversationId = new UUIDFilter();
        }
        return conversationId;
    }

    public void setConversationId(UUIDFilter conversationId) {
        this.conversationId = conversationId;
    }

    public UUIDFilter getCustomerServiceId() {
        return customerServiceId;
    }

    public UUIDFilter customerServiceId() {
        if (customerServiceId == null) {
            customerServiceId = new UUIDFilter();
        }
        return customerServiceId;
    }

    public void setCustomerServiceId(UUIDFilter customerServiceId) {
        this.customerServiceId = customerServiceId;
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
        final FAQsCriteria that = (FAQsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(answers, that.answers) &&
            Objects.equals(question, that.question) &&
            Objects.equals(keyWords, that.keyWords) &&
            Objects.equals(conversationId, that.conversationId) &&
            Objects.equals(customerServiceId, that.customerServiceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answers, question, keyWords, conversationId, customerServiceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FAQsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (answers != null ? "answers=" + answers + ", " : "") +
            (question != null ? "question=" + question + ", " : "") +
            (keyWords != null ? "keyWords=" + keyWords + ", " : "") +
            (conversationId != null ? "conversationId=" + conversationId + ", " : "") +
            (customerServiceId != null ? "customerServiceId=" + customerServiceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
