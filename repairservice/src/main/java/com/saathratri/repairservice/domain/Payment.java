package com.saathratri.repairservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bookings", "payments", "repairRequests" }, allowSetters = true)
    private Customer customer;

    @JsonIgnoreProperties(value = { "payment", "customer" }, allowSetters = true)
    @OneToOne(mappedBy = "payment")
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Payment id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPaymentId() {
        return this.paymentId;
    }

    public Payment paymentId(UUID paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public Payment customerId(UUID customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getBookingId() {
        return this.bookingId;
    }

    public Payment bookingId(UUID bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Payment amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ZonedDateTime getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(ZonedDateTime paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Payment customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        if (this.booking != null) {
            this.booking.setPayment(null);
        }
        if (booking != null) {
            booking.setPayment(this);
        }
        this.booking = booking;
    }

    public Payment booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentId='" + getPaymentId() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", bookingId='" + getBookingId() + "'" +
            ", amount=" + getAmount() +
            ", paymentDate='" + getPaymentDate() + "'" +
            "}";
    }
}
