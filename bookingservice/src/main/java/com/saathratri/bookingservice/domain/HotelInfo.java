package com.saathratri.bookingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HotelInfo.
 */
@Entity
@Table(name = "hotel_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HotelInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "hotel_name")
    private String hotelName;

    @JsonIgnoreProperties(value = { "hotel" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private LoyaltyProgram loyaltyProgram;

    @ManyToOne
    @JsonIgnoreProperties(value = { "customer", "hotels" }, allowSetters = true)
    private Address address;

    @OneToMany(mappedBy = "hotel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "hotel" }, allowSetters = true)
    private Set<Reservation> reservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public HotelInfo id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHotelName() {
        return this.hotelName;
    }

    public HotelInfo hotelName(String hotelName) {
        this.setHotelName(hotelName);
        return this;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public LoyaltyProgram getLoyaltyProgram() {
        return this.loyaltyProgram;
    }

    public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        this.loyaltyProgram = loyaltyProgram;
    }

    public HotelInfo loyaltyProgram(LoyaltyProgram loyaltyProgram) {
        this.setLoyaltyProgram(loyaltyProgram);
        return this;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public HotelInfo address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Set<Reservation> getReservations() {
        return this.reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        if (this.reservations != null) {
            this.reservations.forEach(i -> i.setHotel(null));
        }
        if (reservations != null) {
            reservations.forEach(i -> i.setHotel(this));
        }
        this.reservations = reservations;
    }

    public HotelInfo reservations(Set<Reservation> reservations) {
        this.setReservations(reservations);
        return this;
    }

    public HotelInfo addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setHotel(this);
        return this;
    }

    public HotelInfo removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setHotel(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HotelInfo)) {
            return false;
        }
        return id != null && id.equals(((HotelInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HotelInfo{" +
            "id=" + getId() +
            ", hotelName='" + getHotelName() + "'" +
            "}";
    }
}
