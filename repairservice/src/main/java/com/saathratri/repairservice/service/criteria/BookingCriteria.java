package com.saathratri.repairservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.repairservice.domain.Booking} entity. This class is used
 * in {@link com.saathratri.repairservice.web.rest.BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter bookingId;

    private UUIDFilter customerId;

    private UUIDFilter roomId;

    private LocalDateFilter checkInDate;

    private LocalDateFilter checkOutDate;

    private DoubleFilter totalPrice;

    private UUIDFilter paymentId;

    private UUIDFilter customerId;

    private Boolean distinct;

    public BookingCriteria() {}

    public BookingCriteria(BookingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.roomId = other.roomId == null ? null : other.roomId.copy();
        this.checkInDate = other.checkInDate == null ? null : other.checkInDate.copy();
        this.checkOutDate = other.checkOutDate == null ? null : other.checkOutDate.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BookingCriteria copy() {
        return new BookingCriteria(this);
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

    public UUIDFilter getRoomId() {
        return roomId;
    }

    public UUIDFilter roomId() {
        if (roomId == null) {
            roomId = new UUIDFilter();
        }
        return roomId;
    }

    public void setRoomId(UUIDFilter roomId) {
        this.roomId = roomId;
    }

    public LocalDateFilter getCheckInDate() {
        return checkInDate;
    }

    public LocalDateFilter checkInDate() {
        if (checkInDate == null) {
            checkInDate = new LocalDateFilter();
        }
        return checkInDate;
    }

    public void setCheckInDate(LocalDateFilter checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDateFilter getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDateFilter checkOutDate() {
        if (checkOutDate == null) {
            checkOutDate = new LocalDateFilter();
        }
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDateFilter checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public DoubleFilter getTotalPrice() {
        return totalPrice;
    }

    public DoubleFilter totalPrice() {
        if (totalPrice == null) {
            totalPrice = new DoubleFilter();
        }
        return totalPrice;
    }

    public void setTotalPrice(DoubleFilter totalPrice) {
        this.totalPrice = totalPrice;
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
        final BookingCriteria that = (BookingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(roomId, that.roomId) &&
            Objects.equals(checkInDate, that.checkInDate) &&
            Objects.equals(checkOutDate, that.checkOutDate) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingId, customerId, roomId, checkInDate, checkOutDate, totalPrice, paymentId, customerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (roomId != null ? "roomId=" + roomId + ", " : "") +
            (checkInDate != null ? "checkInDate=" + checkInDate + ", " : "") +
            (checkOutDate != null ? "checkOutDate=" + checkOutDate + ", " : "") +
            (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
