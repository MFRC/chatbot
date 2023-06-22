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
 * A CustomerService.
 */
@Entity
@Table(name = "customer_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerService implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "report_number")
    private Integer reportNumber;

    @JsonIgnoreProperties(value = { "conversation", "customerService" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private FAQs faqs;

    @JsonIgnoreProperties(value = { "conversation", "customerService" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CustomerServiceEntity customerServiceEntity;

    @JsonIgnoreProperties(value = { "conversation", "customerService" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CustomerServiceUser customerServiceUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public CustomerService id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public CustomerService startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public CustomerService endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getReportNumber() {
        return this.reportNumber;
    }

    public CustomerService reportNumber(Integer reportNumber) {
        this.setReportNumber(reportNumber);
        return this;
    }

    public void setReportNumber(Integer reportNumber) {
        this.reportNumber = reportNumber;
    }

    public FAQs getFaqs() {
        return this.faqs;
    }

    public void setFaqs(FAQs fAQs) {
        this.faqs = fAQs;
    }

    public CustomerService faqs(FAQs fAQs) {
        this.setFaqs(fAQs);
        return this;
    }

    public CustomerServiceEntity getCustomerServiceEntity() {
        return this.customerServiceEntity;
    }

    public void setCustomerServiceEntity(CustomerServiceEntity customerServiceEntity) {
        this.customerServiceEntity = customerServiceEntity;
    }

    public CustomerService customerServiceEntity(CustomerServiceEntity customerServiceEntity) {
        this.setCustomerServiceEntity(customerServiceEntity);
        return this;
    }

    public CustomerServiceUser getCustomerServiceUser() {
        return this.customerServiceUser;
    }

    public void setCustomerServiceUser(CustomerServiceUser customerServiceUser) {
        this.customerServiceUser = customerServiceUser;
    }

    public CustomerService customerServiceUser(CustomerServiceUser customerServiceUser) {
        this.setCustomerServiceUser(customerServiceUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerService)) {
            return false;
        }
        return id != null && id.equals(((CustomerService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerService{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", reportNumber=" + getReportNumber() +
            "}";
    }
}
