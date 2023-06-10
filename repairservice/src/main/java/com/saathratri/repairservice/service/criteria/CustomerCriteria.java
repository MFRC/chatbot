package com.saathratri.repairservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.repairservice.domain.Customer} entity. This class is used
 * in {@link com.saathratri.repairservice.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter customerId;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phoneNumber;

    private UUIDFilter bookingsId;

    private UUIDFilter paymentsId;

    private UUIDFilter repairRequestsId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.bookingsId = other.bookingsId == null ? null : other.bookingsId.copy();
        this.paymentsId = other.paymentsId == null ? null : other.paymentsId.copy();
        this.repairRequestsId = other.repairRequestsId == null ? null : other.repairRequestsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public UUIDFilter getCustomerId() {
        return customerId;
    }

    public UUIDFilter customerId() {
        if (customerId == null) {
            customerId = new UUIDFilter();
        }
        return customerId;
    }

    public void setCustomerId(UUIDFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public UUIDFilter getBookingsId() {
        return bookingsId;
    }

    public UUIDFilter bookingsId() {
        if (bookingsId == null) {
            bookingsId = new UUIDFilter();
        }
        return bookingsId;
    }

    public void setBookingsId(UUIDFilter bookingsId) {
        this.bookingsId = bookingsId;
    }

    public UUIDFilter getPaymentsId() {
        return paymentsId;
    }

    public UUIDFilter paymentsId() {
        if (paymentsId == null) {
            paymentsId = new UUIDFilter();
        }
        return paymentsId;
    }

    public void setPaymentsId(UUIDFilter paymentsId) {
        this.paymentsId = paymentsId;
    }

    public UUIDFilter getRepairRequestsId() {
        return repairRequestsId;
    }

    public UUIDFilter repairRequestsId() {
        if (repairRequestsId == null) {
            repairRequestsId = new UUIDFilter();
        }
        return repairRequestsId;
    }

    public void setRepairRequestsId(UUIDFilter repairRequestsId) {
        this.repairRequestsId = repairRequestsId;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(bookingsId, that.bookingsId) &&
            Objects.equals(paymentsId, that.paymentsId) &&
            Objects.equals(repairRequestsId, that.repairRequestsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, firstName, lastName, email, phoneNumber, bookingsId, paymentsId, repairRequestsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (bookingsId != null ? "bookingsId=" + bookingsId + ", " : "") +
            (paymentsId != null ? "paymentsId=" + paymentsId + ", " : "") +
            (repairRequestsId != null ? "repairRequestsId=" + repairRequestsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
