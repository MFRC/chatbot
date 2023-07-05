package com.saathratri.customerservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.customerservice.domain.CustomerService} entity. This class is used
 * in {@link com.saathratri.customerservice.web.rest.CustomerServiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customer-services?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private IntegerFilter reportNumber;

    private UUIDFilter faqsId;

    private UUIDFilter customerServiceEntityId;

    private UUIDFilter customerServiceUserId;

    private Boolean distinct;

    public CustomerServiceCriteria() {}

    public CustomerServiceCriteria(CustomerServiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.reportNumber = other.reportNumber == null ? null : other.reportNumber.copy();
        this.faqsId = other.faqsId == null ? null : other.faqsId.copy();
        this.customerServiceEntityId = other.customerServiceEntityId == null ? null : other.customerServiceEntityId.copy();
        this.customerServiceUserId = other.customerServiceUserId == null ? null : other.customerServiceUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerServiceCriteria copy() {
        return new CustomerServiceCriteria(this);
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

    public InstantFilter getStartDate() {
        return startDate;
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            startDate = new InstantFilter();
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            endDate = new InstantFilter();
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public IntegerFilter getReportNumber() {
        return reportNumber;
    }

    public IntegerFilter reportNumber() {
        if (reportNumber == null) {
            reportNumber = new IntegerFilter();
        }
        return reportNumber;
    }

    public void setReportNumber(IntegerFilter reportNumber) {
        this.reportNumber = reportNumber;
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
        final CustomerServiceCriteria that = (CustomerServiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(reportNumber, that.reportNumber) &&
            Objects.equals(faqsId, that.faqsId) &&
            Objects.equals(customerServiceEntityId, that.customerServiceEntityId) &&
            Objects.equals(customerServiceUserId, that.customerServiceUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, reportNumber, faqsId, customerServiceEntityId, customerServiceUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (reportNumber != null ? "reportNumber=" + reportNumber + ", " : "") +
            (faqsId != null ? "faqsId=" + faqsId + ", " : "") +
            (customerServiceEntityId != null ? "customerServiceEntityId=" + customerServiceEntityId + ", " : "") +
            (customerServiceUserId != null ? "customerServiceUserId=" + customerServiceUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
