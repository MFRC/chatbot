package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.CustomerService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceDTO implements Serializable {

    @NotNull
    private UUID id;

    private Instant startDate;

    private Instant endDate;

    private Integer reportNumber;

    private FAQsDTO faqs;

    private CustomerServiceEntityDTO customerServiceEntity;

    private CustomerServiceUserDTO customerServiceUser;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Integer getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(Integer reportNumber) {
        this.reportNumber = reportNumber;
    }

    public FAQsDTO getFaqs() {
        return faqs;
    }

    public void setFaqs(FAQsDTO faqs) {
        this.faqs = faqs;
    }

    public CustomerServiceEntityDTO getCustomerServiceEntity() {
        return customerServiceEntity;
    }

    public void setCustomerServiceEntity(CustomerServiceEntityDTO customerServiceEntity) {
        this.customerServiceEntity = customerServiceEntity;
    }

    public CustomerServiceUserDTO getCustomerServiceUser() {
        return customerServiceUser;
    }

    public void setCustomerServiceUser(CustomerServiceUserDTO customerServiceUser) {
        this.customerServiceUser = customerServiceUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerServiceDTO)) {
            return false;
        }

        CustomerServiceDTO customerServiceDTO = (CustomerServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceDTO{" +
            "id='" + getId() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", reportNumber=" + getReportNumber() +
            ", faqs=" + getFaqs() +
            ", customerServiceEntity=" + getCustomerServiceEntity() +
            ", customerServiceUser=" + getCustomerServiceUser() +
            "}";
    }
}
