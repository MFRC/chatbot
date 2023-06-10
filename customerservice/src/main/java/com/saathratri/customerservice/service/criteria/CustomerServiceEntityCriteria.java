package com.saathratri.customerservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.customerservice.domain.CustomerServiceEntity} entity. This class is used
 * in {@link com.saathratri.customerservice.web.rest.CustomerServiceEntityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customer-service-entities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceEntityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter reservationNumber;

    private IntegerFilter roomNumber;

    private StringFilter services;

    private LongFilter prices;

    private StringFilter amenities;

    private UUIDFilter conversationId;

    private UUIDFilter customerServiceId;

    private Boolean distinct;

    public CustomerServiceEntityCriteria() {}

    public CustomerServiceEntityCriteria(CustomerServiceEntityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reservationNumber = other.reservationNumber == null ? null : other.reservationNumber.copy();
        this.roomNumber = other.roomNumber == null ? null : other.roomNumber.copy();
        this.services = other.services == null ? null : other.services.copy();
        this.prices = other.prices == null ? null : other.prices.copy();
        this.amenities = other.amenities == null ? null : other.amenities.copy();
        this.conversationId = other.conversationId == null ? null : other.conversationId.copy();
        this.customerServiceId = other.customerServiceId == null ? null : other.customerServiceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerServiceEntityCriteria copy() {
        return new CustomerServiceEntityCriteria(this);
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

    public StringFilter getReservationNumber() {
        return reservationNumber;
    }

    public StringFilter reservationNumber() {
        if (reservationNumber == null) {
            reservationNumber = new StringFilter();
        }
        return reservationNumber;
    }

    public void setReservationNumber(StringFilter reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public IntegerFilter getRoomNumber() {
        return roomNumber;
    }

    public IntegerFilter roomNumber() {
        if (roomNumber == null) {
            roomNumber = new IntegerFilter();
        }
        return roomNumber;
    }

    public void setRoomNumber(IntegerFilter roomNumber) {
        this.roomNumber = roomNumber;
    }

    public StringFilter getServices() {
        return services;
    }

    public StringFilter services() {
        if (services == null) {
            services = new StringFilter();
        }
        return services;
    }

    public void setServices(StringFilter services) {
        this.services = services;
    }

    public LongFilter getPrices() {
        return prices;
    }

    public LongFilter prices() {
        if (prices == null) {
            prices = new LongFilter();
        }
        return prices;
    }

    public void setPrices(LongFilter prices) {
        this.prices = prices;
    }

    public StringFilter getAmenities() {
        return amenities;
    }

    public StringFilter amenities() {
        if (amenities == null) {
            amenities = new StringFilter();
        }
        return amenities;
    }

    public void setAmenities(StringFilter amenities) {
        this.amenities = amenities;
    }

    public UUIDFilter getConversationId() {
        return conversationId;
    }

    public UUIDFilter conversationId() {
        if (conversationId == null) {
            conversationId = new UUIDFilter();
        }
        return conversationId;
    }

    public void setConversationId(UUIDFilter conversationId) {
        this.conversationId = conversationId;
    }

    public UUIDFilter getCustomerServiceId() {
        return customerServiceId;
    }

    public UUIDFilter customerServiceId() {
        if (customerServiceId == null) {
            customerServiceId = new UUIDFilter();
        }
        return customerServiceId;
    }

    public void setCustomerServiceId(UUIDFilter customerServiceId) {
        this.customerServiceId = customerServiceId;
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
        final CustomerServiceEntityCriteria that = (CustomerServiceEntityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reservationNumber, that.reservationNumber) &&
            Objects.equals(roomNumber, that.roomNumber) &&
            Objects.equals(services, that.services) &&
            Objects.equals(prices, that.prices) &&
            Objects.equals(amenities, that.amenities) &&
            Objects.equals(conversationId, that.conversationId) &&
            Objects.equals(customerServiceId, that.customerServiceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservationNumber, roomNumber, services, prices, amenities, conversationId, customerServiceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceEntityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reservationNumber != null ? "reservationNumber=" + reservationNumber + ", " : "") +
            (roomNumber != null ? "roomNumber=" + roomNumber + ", " : "") +
            (services != null ? "services=" + services + ", " : "") +
            (prices != null ? "prices=" + prices + ", " : "") +
            (amenities != null ? "amenities=" + amenities + ", " : "") +
            (conversationId != null ? "conversationId=" + conversationId + ", " : "") +
            (customerServiceId != null ? "customerServiceId=" + customerServiceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
