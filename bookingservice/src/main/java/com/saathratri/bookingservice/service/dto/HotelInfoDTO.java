package com.saathratri.bookingservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.bookingservice.domain.HotelInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HotelInfoDTO implements Serializable {

    @NotNull
    private UUID id;

    private String hotelName;

    private LoyaltyProgramDTO loyaltyProgram;

    private AddressDTO address;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public LoyaltyProgramDTO getLoyaltyProgram() {
        return loyaltyProgram;
    }

    public void setLoyaltyProgram(LoyaltyProgramDTO loyaltyProgram) {
        this.loyaltyProgram = loyaltyProgram;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HotelInfoDTO)) {
            return false;
        }

        HotelInfoDTO hotelInfoDTO = (HotelInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hotelInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HotelInfoDTO{" +
            "id='" + getId() + "'" +
            ", hotelName='" + getHotelName() + "'" +
            ", loyaltyProgram=" + getLoyaltyProgram() +
            ", address=" + getAddress() +
            "}";
    }
}
