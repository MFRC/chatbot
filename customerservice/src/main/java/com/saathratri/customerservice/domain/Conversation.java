package com.saathratri.customerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conversation.
 */
@Entity
@Table(name = "conversation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "question")
    private String question;

    @Column(name = "answers")
    private String answers;

    @Column(name = "reservation_number")
    private String reservationNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "key_words")
    private String keyWords;

    @JsonIgnoreProperties(value = { "report", "conversation" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private End end;

    @JsonIgnoreProperties(value = { "conversation", "customerService" }, allowSetters = true)
    @OneToOne(mappedBy = "conversation")
    private FAQs faqs;

    @JsonIgnoreProperties(value = { "conversation", "customerService" }, allowSetters = true)
    @OneToOne(mappedBy = "conversation")
    private CustomerServiceEntity customerServiceEntity;

    @JsonIgnoreProperties(value = { "conversation", "customerService" }, allowSetters = true)
    @OneToOne(mappedBy = "conversation")
    private CustomerServiceUser customerServiceUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Conversation id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public Conversation question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswers() {
        return this.answers;
    }

    public Conversation answers(String answers) {
        this.setAnswers(answers);
        return this;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getReservationNumber() {
        return this.reservationNumber;
    }

    public Conversation reservationNumber(String reservationNumber) {
        this.setReservationNumber(reservationNumber);
        return this;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Conversation phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Conversation startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Conversation endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getKeyWords() {
        return this.keyWords;
    }

    public Conversation keyWords(String keyWords) {
        this.setKeyWords(keyWords);
        return this;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public End getEnd() {
        return this.end;
    }

    public void setEnd(End end) {
        this.end = end;
    }

    public Conversation end(End end) {
        this.setEnd(end);
        return this;
    }

    public FAQs getFaqs() {
        return this.faqs;
    }

    public void setFaqs(FAQs fAQs) {
        if (this.faqs != null) {
            this.faqs.setConversation(null);
        }
        if (fAQs != null) {
            fAQs.setConversation(this);
        }
        this.faqs = fAQs;
    }

    public Conversation faqs(FAQs fAQs) {
        this.setFaqs(fAQs);
        return this;
    }

    public CustomerServiceEntity getCustomerServiceEntity() {
        return this.customerServiceEntity;
    }

    public void setCustomerServiceEntity(CustomerServiceEntity customerServiceEntity) {
        if (this.customerServiceEntity != null) {
            this.customerServiceEntity.setConversation(null);
        }
        if (customerServiceEntity != null) {
            customerServiceEntity.setConversation(this);
        }
        this.customerServiceEntity = customerServiceEntity;
    }

    public Conversation customerServiceEntity(CustomerServiceEntity customerServiceEntity) {
        this.setCustomerServiceEntity(customerServiceEntity);
        return this;
    }

    public CustomerServiceUser getCustomerServiceUser() {
        return this.customerServiceUser;
    }

    public void setCustomerServiceUser(CustomerServiceUser customerServiceUser) {
        if (this.customerServiceUser != null) {
            this.customerServiceUser.setConversation(null);
        }
        if (customerServiceUser != null) {
            customerServiceUser.setConversation(this);
        }
        this.customerServiceUser = customerServiceUser;
    }

    public Conversation customerServiceUser(CustomerServiceUser customerServiceUser) {
        this.setCustomerServiceUser(customerServiceUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conversation)) {
            return false;
        }
        return id != null && id.equals(((Conversation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conversation{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", answers='" + getAnswers() + "'" +
            ", reservationNumber='" + getReservationNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", keyWords='" + getKeyWords() + "'" +
            "}";
    }
}
