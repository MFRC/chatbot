package com.saathratri.repairservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saathratri.repairservice.domain.enumeration.RepairStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RepairRequest.
 */
@Entity
@Table(name = "repair_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RepairRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "repair_request_id")
    private UUID repairRequestId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RepairStatus status;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "date_updated")
    private ZonedDateTime dateUpdated;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bookings", "payments", "repairRequests" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public RepairRequest id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRepairRequestId() {
        return this.repairRequestId;
    }

    public RepairRequest repairRequestId(UUID repairRequestId) {
        this.setRepairRequestId(repairRequestId);
        return this;
    }

    public void setRepairRequestId(UUID repairRequestId) {
        this.repairRequestId = repairRequestId;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public RepairRequest customerId(UUID customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getRoomNumber() {
        return this.roomNumber;
    }

    public RepairRequest roomNumber(String roomNumber) {
        this.setRoomNumber(roomNumber);
        return this;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public RepairRequest description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RepairStatus getStatus() {
        return this.status;
    }

    public RepairRequest status(RepairStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(RepairStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDateCreated() {
        return this.dateCreated;
    }

    public RepairRequest dateCreated(ZonedDateTime dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getDateUpdated() {
        return this.dateUpdated;
    }

    public RepairRequest dateUpdated(ZonedDateTime dateUpdated) {
        this.setDateUpdated(dateUpdated);
        return this;
    }

    public void setDateUpdated(ZonedDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RepairRequest customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepairRequest)) {
            return false;
        }
        return id != null && id.equals(((RepairRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RepairRequest{" +
            "id=" + getId() +
            ", repairRequestId='" + getRepairRequestId() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", roomNumber='" + getRoomNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
