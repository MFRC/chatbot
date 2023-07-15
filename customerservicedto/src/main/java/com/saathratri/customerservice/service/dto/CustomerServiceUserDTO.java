package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.CustomerServiceUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceUserDTO implements Serializable {

  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  private String reservationNumber;

  private String roomNumber;

  private ConversationDTO conversation;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getReservationNumber() {
    return reservationNumber;
  }

  public void setReservationNumber(String reservationNumber) {
    this.reservationNumber = reservationNumber;
  }

  public String getRoomNumber() {
    return roomNumber;
  }

  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
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
    if (!(o instanceof CustomerServiceUserDTO)) {
      return false;
    }

    CustomerServiceUserDTO customerServiceUserDTO = (CustomerServiceUserDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, customerServiceUserDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceUserDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", reservationNumber='" + getReservationNumber() + "'" +
            ", roomNumber='" + getRoomNumber() + "'" +
            ", conversation=" + getConversation() +
            "}";
    }
}
