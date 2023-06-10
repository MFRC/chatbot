package com.saathratri.repairservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.repairservice.domain.Payment} entity. This class is used
 * in {@link com.saathratri.repairservice.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter paymentId;

    private UUIDFilter customerId;

    private UUIDFilter bookingId;

    private DoubleFilter amount;

    private ZonedDateTimeFilter paymentDate;

    private UUIDFilter customerId;

    private UUIDFilter bookingId;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public UUIDFilter getPaymentId() {
        return paymentId;
    }

    public UUIDFilter paymentId() {
        if (paymentId == null) {
            paymentId = new UUIDFilter();
        }
        return paymentId;
    }

    public void setPaymentId(UUIDFilter paymentId) {
        this.paymentId = paymentId;
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

    public UUIDFilter getBookingId() {
        return bookingId;
    }

    public UUIDFilter bookingId() {
        if (bookingId == null) {
            bookingId = new UUIDFilter();
        }
        return bookingId;
    }

    public void setBookingId(UUIDFilter bookingId) {
        this.bookingId = bookingId;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public DoubleFilter amount() {
        if (amount == null) {
            amount = new DoubleFilter();
        }
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public ZonedDateTimeFilter getPaymentDate() {
        return paymentDate;
    }

    public ZonedDateTimeFilter paymentDate() {
        if (paymentDate == null) {
            paymentDate = new ZonedDateTimeFilter();
        }
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTimeFilter paymentDate) {
        this.paymentDate = paymentDate;
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

    public UUIDFilter getBookingId() {
        return bookingId;
    }

    public UUIDFilter bookingId() {
        if (bookingId == null) {
            bookingId = new UUIDFilter();
        }
        return bookingId;
    }

    public void setBookingId(UUIDFilter bookingId) {
        this.bookingId = bookingId;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentId, customerId, bookingId, amount, paymentDate, customerId, bookingId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
