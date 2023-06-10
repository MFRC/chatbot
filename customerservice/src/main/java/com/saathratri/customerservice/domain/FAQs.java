package com.saathratri.customerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FAQs.
 */
@Entity
@Table(name = "fa_qs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FAQs implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "answers")
    private String answers;

    @Column(name = "question")
    private String question;

    @Column(name = "key_words")
    private String keyWords;

    @JsonIgnoreProperties(value = { "end", "faqs", "customerServiceEntity", "customerServiceUser" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Conversation conversation;

    @JsonIgnoreProperties(value = { "faqs", "customerServiceEntity", "customerServiceUser" }, allowSetters = true)
    @OneToOne(mappedBy = "faqs")
    private CustomerService customerService;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public FAQs id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAnswers() {
        return this.answers;
    }

    public FAQs answers(String answers) {
        this.setAnswers(answers);
        return this;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return this.question;
    }

    public FAQs question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getKeyWords() {
        return this.keyWords;
    }

    public FAQs keyWords(String keyWords) {
        this.setKeyWords(keyWords);
        return this;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public Conversation getConversation() {
        return this.conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public FAQs conversation(Conversation conversation) {
        this.setConversation(conversation);
        return this;
    }

    public CustomerService getCustomerService() {
        return this.customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        if (this.customerService != null) {
            this.customerService.setFaqs(null);
        }
        if (customerService != null) {
            customerService.setFaqs(this);
        }
        this.customerService = customerService;
    }

    public FAQs customerService(CustomerService customerService) {
        this.setCustomerService(customerService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FAQs)) {
            return false;
        }
        return id != null && id.equals(((FAQs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FAQs{" +
            "id=" + getId() +
            ", answers='" + getAnswers() + "'" +
            ", question='" + getQuestion() + "'" +
            ", keyWords='" + getKeyWords() + "'" +
            "}";
    }
}
