package com.saathratri.repairservice.service.criteria;

import com.saathratri.repairservice.domain.enumeration.RepairStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.repairservice.domain.RepairRequest} entity. This class is used
 * in {@link com.saathratri.repairservice.web.rest.RepairRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /repair-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RepairRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RepairStatus
     */
    public static class RepairStatusFilter extends Filter<RepairStatus> {

        public RepairStatusFilter() {}

        public RepairStatusFilter(RepairStatusFilter filter) {
            super(filter);
        }

        @Override
        public RepairStatusFilter copy() {
            return new RepairStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter repairRequestId;

    private UUIDFilter customerId;

    private StringFilter roomNumber;

    private StringFilter description;

    private RepairStatusFilter status;

    private ZonedDateTimeFilter dateCreated;

    private ZonedDateTimeFilter dateUpdated;

    private UUIDFilter customerId;

    private Boolean distinct;

    public RepairRequestCriteria() {}

    public RepairRequestCriteria(RepairRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.repairRequestId = other.repairRequestId == null ? null : other.repairRequestId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.roomNumber = other.roomNumber == null ? null : other.roomNumber.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.dateUpdated = other.dateUpdated == null ? null : other.dateUpdated.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RepairRequestCriteria copy() {
        return new RepairRequestCriteria(this);
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

    public UUIDFilter getRepairRequestId() {
        return repairRequestId;
    }

    public UUIDFilter repairRequestId() {
        if (repairRequestId == null) {
            repairRequestId = new UUIDFilter();
        }
        return repairRequestId;
    }

    public void setRepairRequestId(UUIDFilter repairRequestId) {
        this.repairRequestId = repairRequestId;
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public RepairStatusFilter getStatus() {
        return status;
    }

    public RepairStatusFilter status() {
        if (status == null) {
            status = new RepairStatusFilter();
        }
        return status;
    }

    public void setStatus(RepairStatusFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getDateCreated() {
        return dateCreated;
    }

    public ZonedDateTimeFilter dateCreated() {
        if (dateCreated == null) {
            dateCreated = new ZonedDateTimeFilter();
        }
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTimeFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTimeFilter getDateUpdated() {
        return dateUpdated;
    }

    public ZonedDateTimeFilter dateUpdated() {
        if (dateUpdated == null) {
            dateUpdated = new ZonedDateTimeFilter();
        }
        return dateUpdated;
    }

    public void setDateUpdated(ZonedDateTimeFilter dateUpdated) {
        this.dateUpdated = dateUpdated;
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
        final RepairRequestCriteria that = (RepairRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(repairRequestId, that.repairRequestId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(roomNumber, that.roomNumber) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(dateUpdated, that.dateUpdated) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            repairRequestId,
            customerId,
            roomNumber,
            description,
            status,
            dateCreated,
            dateUpdated,
            customerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RepairRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (repairRequestId != null ? "repairRequestId=" + repairRequestId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (roomNumber != null ? "roomNumber=" + roomNumber + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
            (dateUpdated != null ? "dateUpdated=" + dateUpdated + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
