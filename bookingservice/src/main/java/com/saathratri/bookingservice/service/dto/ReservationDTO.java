package com.saathratri.bookingservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.bookingservice.domain.Reservation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservationDTO implements Serializable {

    @NotNull
    private UUID id;

    private String accountNumber;

    private String status;

    private String ratePlan;

    private Long arrivalDate;

    private Long departureDate;

    private Long checkInDateTime;

    private Long checkOutDateTime;

    private String roomType;

    private String roomNumber;

    private Integer adults;

    private Integer children;

    private Boolean crib;

    private Boolean rollaway;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private HotelInfoDTO hotel;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRatePlan() {
        return ratePlan;
    }

    public void setRatePlan(String ratePlan) {
        this.ratePlan = ratePlan;
    }

    public Long getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Long arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Long getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Long departureDate) {
        this.departureDate = departureDate;
    }

    public Long getCheckInDateTime() {
        return checkInDateTime;
    }

    public void setCheckInDateTime(Long checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }

    public Long getCheckOutDateTime() {
        return checkOutDateTime;
    }

    public void setCheckOutDateTime(Long checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getAdults() {
        return adults;
    }

    public void setAdults(Integer adults) {
        this.adults = adults;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Boolean getCrib() {
        return crib;
    }

    public void setCrib(Boolean crib) {
        this.crib = crib;
    }

    public Boolean getRollaway() {
        return rollaway;
    }

    public void setRollaway(Boolean rollaway) {
        this.rollaway = rollaway;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HotelInfoDTO getHotel() {
        return hotel;
    }

    public void setHotel(HotelInfoDTO hotel) {
        this.hotel = hotel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationDTO)) {
            return false;
        }

        ReservationDTO reservationDTO = (ReservationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reservationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservationDTO{" +
            "id='" + getId() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", ratePlan='" + getRatePlan() + "'" +
            ", arrivalDate=" + getArrivalDate() +
            ", departureDate=" + getDepartureDate() +
            ", checkInDateTime=" + getCheckInDateTime() +
            ", checkOutDateTime=" + getCheckOutDateTime() +
            ", roomType='" + getRoomType() + "'" +
            ", roomNumber='" + getRoomNumber() + "'" +
            ", adults=" + getAdults() +
            ", children=" + getChildren() +
            ", crib='" + getCrib() + "'" +
            ", rollaway='" + getRollaway() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", hotel=" + getHotel() +
            "}";
    }
}
