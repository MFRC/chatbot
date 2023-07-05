package com.saathratri.bookingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "rate_plan")
    private String ratePlan;

    @Column(name = "arrival_date")
    private Long arrivalDate;

    @Column(name = "departure_date")
    private Long departureDate;

    @Column(name = "check_in_date_time")
    private Long checkInDateTime;

    @Column(name = "check_out_date_time")
    private Long checkOutDateTime;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "adults")
    private Integer adults;

    @Column(name = "children")
    private Integer children;

    @Column(name = "crib")
    private Boolean crib;

    @Column(name = "rollaway")
    private Boolean rollaway;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JsonIgnoreProperties(value = { "loyaltyProgram", "address", "reservations" }, allowSetters = true)
    private HotelInfo hotel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Reservation id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public Reservation accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return this.status;
    }

    public Reservation status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRatePlan() {
        return this.ratePlan;
    }

    public Reservation ratePlan(String ratePlan) {
        this.setRatePlan(ratePlan);
        return this;
    }

    public void setRatePlan(String ratePlan) {
        this.ratePlan = ratePlan;
    }

    public Long getArrivalDate() {
        return this.arrivalDate;
    }

    public Reservation arrivalDate(Long arrivalDate) {
        this.setArrivalDate(arrivalDate);
        return this;
    }

    public void setArrivalDate(Long arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Long getDepartureDate() {
        return this.departureDate;
    }

    public Reservation departureDate(Long departureDate) {
        this.setDepartureDate(departureDate);
        return this;
    }

    public void setDepartureDate(Long departureDate) {
        this.departureDate = departureDate;
    }

    public Long getCheckInDateTime() {
        return this.checkInDateTime;
    }

    public Reservation checkInDateTime(Long checkInDateTime) {
        this.setCheckInDateTime(checkInDateTime);
        return this;
    }

    public void setCheckInDateTime(Long checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }

    public Long getCheckOutDateTime() {
        return this.checkOutDateTime;
    }

    public Reservation checkOutDateTime(Long checkOutDateTime) {
        this.setCheckOutDateTime(checkOutDateTime);
        return this;
    }

    public void setCheckOutDateTime(Long checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public Reservation roomType(String roomType) {
        this.setRoomType(roomType);
        return this;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return this.roomNumber;
    }

    public Reservation roomNumber(String roomNumber) {
        this.setRoomNumber(roomNumber);
        return this;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getAdults() {
        return this.adults;
    }

    public Reservation adults(Integer adults) {
        this.setAdults(adults);
        return this;
    }

    public void setAdults(Integer adults) {
        this.adults = adults;
    }

    public Integer getChildren() {
        return this.children;
    }

    public Reservation children(Integer children) {
        this.setChildren(children);
        return this;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Boolean getCrib() {
        return this.crib;
    }

    public Reservation crib(Boolean crib) {
        this.setCrib(crib);
        return this;
    }

    public void setCrib(Boolean crib) {
        this.crib = crib;
    }

    public Boolean getRollaway() {
        return this.rollaway;
    }

    public Reservation rollaway(Boolean rollaway) {
        this.setRollaway(rollaway);
        return this;
    }

    public void setRollaway(Boolean rollaway) {
        this.rollaway = rollaway;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Reservation firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Reservation lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return this.phone;
    }

    public Reservation phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Reservation email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HotelInfo getHotel() {
        return this.hotel;
    }

    public void setHotel(HotelInfo hotelInfo) {
        this.hotel = hotelInfo;
    }

    public Reservation hotel(HotelInfo hotelInfo) {
        this.setHotel(hotelInfo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
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
            "}";
    }
}
