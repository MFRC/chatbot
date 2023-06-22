package com.saathratri.customerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A End.
 */
@Entity
@Table(name = "jhi_end")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class End implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "close_message")
    private String closeMessage;

    @Column(name = "more_help")
    private String moreHelp;

    @JsonIgnoreProperties(value = { "end" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Report report;

    @JsonIgnoreProperties(value = { "end", "faqs", "customerServiceEntity", "customerServiceUser" }, allowSetters = true)
    @OneToOne(mappedBy = "end")
    private Conversation conversation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public End id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCloseMessage() {
        return this.closeMessage;
    }

    public End closeMessage(String closeMessage) {
        this.setCloseMessage(closeMessage);
        return this;
    }

    public void setCloseMessage(String closeMessage) {
        this.closeMessage = closeMessage;
    }

    public String getMoreHelp() {
        return this.moreHelp;
    }

    public End moreHelp(String moreHelp) {
        this.setMoreHelp(moreHelp);
        return this;
    }

    public void setMoreHelp(String moreHelp) {
        this.moreHelp = moreHelp;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public End report(Report report) {
        this.setReport(report);
        return this;
    }

    public Conversation getConversation() {
        return this.conversation;
    }

    public void setConversation(Conversation conversation) {
        if (this.conversation != null) {
            this.conversation.setEnd(null);
        }
        if (conversation != null) {
            conversation.setEnd(this);
        }
        this.conversation = conversation;
    }

    public End conversation(Conversation conversation) {
        this.setConversation(conversation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof End)) {
            return false;
        }
        return id != null && id.equals(((End) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "End{" +
            "id=" + getId() +
            ", closeMessage='" + getCloseMessage() + "'" +
            ", moreHelp='" + getMoreHelp() + "'" +
            "}";
    }
}
