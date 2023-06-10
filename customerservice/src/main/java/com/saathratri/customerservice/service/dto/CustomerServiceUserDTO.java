package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.CustomerServiceUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceUserDTO implements Serializable {

    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerServiceUserDTO)) {
            return false;
        }

        CustomerServiceUserDTO customerServiceUserDTO = (CustomerServiceUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerServiceUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceUserDTO{" +
            "id='" + getId() + "'" +
            "}";
    }
}
