package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.CustomerServiceEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceEntityDTO implements Serializable {

  private Long id;

  private String reservationNumber;

  private Integer roomNumber;

  private String services;

  private Long prices;

  private String amenities;

  private ConversationDTO conversation;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReservationNumber() {
    return reservationNumber;
  }

  public void setReservationNumber(String reservationNumber) {
    this.reservationNumber = reservationNumber;
  }

  public Integer getRoomNumber() {
    return roomNumber;
  }

  public void setRoomNumber(Integer roomNumber) {
    this.roomNumber = roomNumber;
  }

  public String getServices() {
    return services;
  }

  public void setServices(String services) {
    this.services = services;
  }

  public Long getPrices() {
    return prices;
  }

  public void setPrices(Long prices) {
    this.prices = prices;
  }

  public String getAmenities() {
    return amenities;
  }

  public void setAmenities(String amenities) {
    this.amenities = amenities;
  }

  public ConversationDTO getConversation() {
    return conversation;
  }

  public void setConversation(ConversationDTO conversation) {
    this.conversation = conversation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomerServiceEntityDTO)) {
      return false;
    }

    CustomerServiceEntityDTO customerServiceEntityDTO = (CustomerServiceEntityDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, customerServiceEntityDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceEntityDTO{" +
            "id=" + getId() +
            ", reservationNumber='" + getReservationNumber() + "'" +
            ", roomNumber=" + getRoomNumber() +
            ", services='" + getServices() + "'" +
            ", prices=" + getPrices() +
            ", amenities='" + getAmenities() + "'" +
            ", conversation=" + getConversation() +
            "}";
    }
}
