package com.saathratri.bookingservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.bookingservice.domain.CustomerInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerInfoDTO implements Serializable {

    @NotNull
    private UUID id;

    private UUID customerID;

    private String firstName;

    private String lastName;

    private AddressDTO address;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
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
        if (!(o instanceof CustomerInfoDTO)) {
            return false;
        }

        CustomerInfoDTO customerInfoDTO = (CustomerInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerInfoDTO{" +
            "id='" + getId() + "'" +
            ", customerID='" + getCustomerID() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", address=" + getAddress() +
            "}";
    }
}
