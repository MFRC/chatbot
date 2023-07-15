package com.saathratri.repairservice.service.dto;

import com.saathratri.repairservice.domain.enumeration.RepairStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.repairservice.domain.RepairRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RepairRequestDTO implements Serializable {

  private Long id;

  private String roomNumber;

  private String description;

  private RepairStatus status;

  private LocalDate dateCreated;

  private LocalDate dateUpdated;

  private CustomerDTO customer;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRoomNumber() {
    return roomNumber;
  }

  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public RepairStatus getStatus() {
    return status;
  }

  public void setStatus(RepairStatus status) {
    this.status = status;
  }

  public LocalDate getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDate dateCreated) {
    this.dateCreated = dateCreated;
  }

  public LocalDate getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(LocalDate dateUpdated) {
    this.dateUpdated = dateUpdated;
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
    if (!(o instanceof RepairRequestDTO)) {
      return false;
    }

    RepairRequestDTO repairRequestDTO = (RepairRequestDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, repairRequestDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "RepairRequestDTO{" +
            "id=" + getId() +
            ", roomNumber='" + getRoomNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", customer=" + getCustomer() +
            "}";
    }
}
