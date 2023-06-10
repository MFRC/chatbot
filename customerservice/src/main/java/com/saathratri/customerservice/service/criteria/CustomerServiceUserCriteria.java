package com.saathratri.customerservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.customerservice.domain.CustomerServiceUser} entity. This class is used
 * in {@link com.saathratri.customerservice.web.rest.CustomerServiceUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customer-service-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phoneNumber;

    private StringFilter reservationNumber;

    private StringFilter roomNumber;

    private UUIDFilter conversationId;

    private UUIDFilter customerServiceId;

    private Boolean distinct;

    public CustomerServiceUserCriteria() {}

    public CustomerServiceUserCriteria(CustomerServiceUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.reservationNumber = other.reservationNumber == null ? null : other.reservationNumber.copy();
        this.roomNumber = other.roomNumber == null ? null : other.roomNumber.copy();
        this.conversationId = other.conversationId == null ? null : other.conversationId.copy();
        this.customerServiceId = other.customerServiceId == null ? null : other.customerServiceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerServiceUserCriteria copy() {
        return new CustomerServiceUserCriteria(this);
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

    public StringFilter getRoomNumber() {
        return roomNumber;
    }

    public StringFilter roomNumber() {
        if (roomNumber == null) {
            roomNumber = new StringFilter();
        }
        return roomNumber;
    }

    public void setRoomNumber(StringFilter roomNumber) {
        this.roomNumber = roomNumber;
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
        final CustomerServiceUserCriteria that = (CustomerServiceUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(reservationNumber, that.reservationNumber) &&
            Objects.equals(roomNumber, that.roomNumber) &&
            Objects.equals(conversationId, that.conversationId) &&
            Objects.equals(customerServiceId, that.customerServiceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            email,
            phoneNumber,
            reservationNumber,
            roomNumber,
            conversationId,
            customerServiceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (reservationNumber != null ? "reservationNumber=" + reservationNumber + ", " : "") +
            (roomNumber != null ? "roomNumber=" + roomNumber + ", " : "") +
            (conversationId != null ? "conversationId=" + conversationId + ", " : "") +
            (customerServiceId != null ? "customerServiceId=" + customerServiceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
