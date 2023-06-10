package com.saathratri.bookingservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.bookingservice.domain.Address} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AddressDTO implements Serializable {

    @NotNull
    private UUID id;

    private String addressStreet1;

    private String addressStreet2;

    private String addressCity;

    private String addressStateOrProvince;

    private String addressCountry;

    private String addressZipOrPostalCode;

    private String addressIsHomeOrBusiness;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAddressStreet1() {
        return addressStreet1;
    }

    public void setAddressStreet1(String addressStreet1) {
        this.addressStreet1 = addressStreet1;
    }

    public String getAddressStreet2() {
        return addressStreet2;
    }

    public void setAddressStreet2(String addressStreet2) {
        this.addressStreet2 = addressStreet2;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressStateOrProvince() {
        return addressStateOrProvince;
    }

    public void setAddressStateOrProvince(String addressStateOrProvince) {
        this.addressStateOrProvince = addressStateOrProvince;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressZipOrPostalCode() {
        return addressZipOrPostalCode;
    }

    public void setAddressZipOrPostalCode(String addressZipOrPostalCode) {
        this.addressZipOrPostalCode = addressZipOrPostalCode;
    }

    public String getAddressIsHomeOrBusiness() {
        return addressIsHomeOrBusiness;
    }

    public void setAddressIsHomeOrBusiness(String addressIsHomeOrBusiness) {
        this.addressIsHomeOrBusiness = addressIsHomeOrBusiness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressDTO)) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, addressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressDTO{" +
            "id='" + getId() + "'" +
            ", addressStreet1='" + getAddressStreet1() + "'" +
            ", addressStreet2='" + getAddressStreet2() + "'" +
            ", addressCity='" + getAddressCity() + "'" +
            ", addressStateOrProvince='" + getAddressStateOrProvince() + "'" +
            ", addressCountry='" + getAddressCountry() + "'" +
            ", addressZipOrPostalCode='" + getAddressZipOrPostalCode() + "'" +
            ", addressIsHomeOrBusiness='" + getAddressIsHomeOrBusiness() + "'" +
            "}";
    }
}
