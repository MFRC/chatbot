package com.saathratri.repairservice.service.dto;

import com.saathratri.repairservice.domain.enumeration.RepairStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.repairservice.domain.RepairRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RepairRequestDTO implements Serializable {

    @NotNull
    private UUID id;

    private UUID repairRequestId;

    private String roomNumber;

    private String description;

    private RepairStatus status;

    private ZonedDateTime dateCreated;

    private ZonedDateTime dateUpdated;

    private CustomerDTO customer;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRepairRequestId() {
        return repairRequestId;
    }

    public void setRepairRequestId(UUID repairRequestId) {
        this.repairRequestId = repairRequestId;
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

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(ZonedDateTime dateUpdated) {
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
            "id='" + getId() + "'" +
            ", repairRequestId='" + getRepairRequestId() + "'" +
            ", roomNumber='" + getRoomNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", customer=" + getCustomer() +
            "}";
    }
}
