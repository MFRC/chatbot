package com.saathratri.bookingservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LoyaltyProgram.
 */
@Entity
@Table(name = "loyalty_program")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoyaltyProgram implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "loyalty_program_name")
    private String loyaltyProgramName;

    @Column(name = "loyalty_program_member")
    private Boolean loyaltyProgramMember;

    @Column(name = "loyalty_program_number")
    private String loyaltyProgramNumber;

    @Column(name = "loyalty_program_tier")
    private String loyaltyProgramTier;

    @JsonIgnoreProperties(value = { "loyaltyProgram", "address", "reservations" }, allowSetters = true)
    @OneToOne(mappedBy = "loyaltyProgram")
    private HotelInfo hotel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public LoyaltyProgram id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLoyaltyProgramName() {
        return this.loyaltyProgramName;
    }

    public LoyaltyProgram loyaltyProgramName(String loyaltyProgramName) {
        this.setLoyaltyProgramName(loyaltyProgramName);
        return this;
    }

    public void setLoyaltyProgramName(String loyaltyProgramName) {
        this.loyaltyProgramName = loyaltyProgramName;
    }

    public Boolean getLoyaltyProgramMember() {
        return this.loyaltyProgramMember;
    }

    public LoyaltyProgram loyaltyProgramMember(Boolean loyaltyProgramMember) {
        this.setLoyaltyProgramMember(loyaltyProgramMember);
        return this;
    }

    public void setLoyaltyProgramMember(Boolean loyaltyProgramMember) {
        this.loyaltyProgramMember = loyaltyProgramMember;
    }

    public String getLoyaltyProgramNumber() {
        return this.loyaltyProgramNumber;
    }

    public LoyaltyProgram loyaltyProgramNumber(String loyaltyProgramNumber) {
        this.setLoyaltyProgramNumber(loyaltyProgramNumber);
        return this;
    }

    public void setLoyaltyProgramNumber(String loyaltyProgramNumber) {
        this.loyaltyProgramNumber = loyaltyProgramNumber;
    }

    public String getLoyaltyProgramTier() {
        return this.loyaltyProgramTier;
    }

    public LoyaltyProgram loyaltyProgramTier(String loyaltyProgramTier) {
        this.setLoyaltyProgramTier(loyaltyProgramTier);
        return this;
    }

    public void setLoyaltyProgramTier(String loyaltyProgramTier) {
        this.loyaltyProgramTier = loyaltyProgramTier;
    }

    public HotelInfo getHotel() {
        return this.hotel;
    }

    public void setHotel(HotelInfo hotelInfo) {
        if (this.hotel != null) {
            this.hotel.setLoyaltyProgram(null);
        }
        if (hotelInfo != null) {
            hotelInfo.setLoyaltyProgram(this);
        }
        this.hotel = hotelInfo;
    }

    public LoyaltyProgram hotel(HotelInfo hotelInfo) {
        this.setHotel(hotelInfo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoyaltyProgram)) {
            return false;
        }
        return id != null && id.equals(((LoyaltyProgram) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyProgram{" +
            "id=" + getId() +
            ", loyaltyProgramName='" + getLoyaltyProgramName() + "'" +
            ", loyaltyProgramMember='" + getLoyaltyProgramMember() + "'" +
            ", loyaltyProgramNumber='" + getLoyaltyProgramNumber() + "'" +
            ", loyaltyProgramTier='" + getLoyaltyProgramTier() + "'" +
            "}";
    }
}
