package com.saathratri.repairservice.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.repairservice.domain.Booking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingDTO implements Serializable {

  private Long id;

  private String roomId;

  private LocalDate checkInDate;

  private LocalDate checkOutDate;

  private Double totalPrice;

  private PaymentDTO payment;

  private CustomerDTO customer;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public LocalDate getCheckInDate() {
    return checkInDate;
  }

  public void setCheckInDate(LocalDate checkInDate) {
    this.checkInDate = checkInDate;
  }

  public LocalDate getCheckOutDate() {
    return checkOutDate;
  }

  public void setCheckOutDate(LocalDate checkOutDate) {
    this.checkOutDate = checkOutDate;
  }

  public Double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public PaymentDTO getPayment() {
    return payment;
  }

  public void setPayment(PaymentDTO payment) {
    this.payment = payment;
  }

  public CustomerDTO getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerDTO customer) {
    this.customer = customer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BookingDTO)) {
      return false;
    }

    BookingDTO bookingDTO = (BookingDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, bookingDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", roomId='" + getRoomId() + "'" +
            ", checkInDate='" + getCheckInDate() + "'" +
            ", checkOutDate='" + getCheckOutDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", payment=" + getPayment() +
            ", customer=" + getCustomer() +
            "}";
    }
}
