package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.CustomerService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceDTO implements Serializable {

  private Long id;

  private LocalDate startDate;

  private LocalDate endDate;

  private Integer reportNumber;

  private FaqDTO faq;

  private CustomerServiceEntityDTO customerServiceEntity;

  private CustomerServiceUserDTO customerServiceUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Integer getReportNumber() {
    return reportNumber;
  }

  public void setReportNumber(Integer reportNumber) {
    this.reportNumber = reportNumber;
  }

  public FaqDTO getFaq() {
    return faq;
  }

  public void setFaq(FaqDTO faq) {
    this.faq = faq;
  }

  public CustomerServiceEntityDTO getCustomerServiceEntity() {
    return customerServiceEntity;
  }

  public void setCustomerServiceEntity(
    CustomerServiceEntityDTO customerServiceEntity
  ) {
    this.customerServiceEntity = customerServiceEntity;
  }

  public CustomerServiceUserDTO getCustomerServiceUser() {
    return customerServiceUser;
  }

  public void setCustomerServiceUser(
    CustomerServiceUserDTO customerServiceUser
  ) {
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
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", reportNumber=" + getReportNumber() +
            ", faq=" + getFaq() +
            ", customerServiceEntity=" + getCustomerServiceEntity() +
            ", customerServiceUser=" + getCustomerServiceUser() +
            "}";
    }
}
