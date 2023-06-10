package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.CustomerService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceDTO implements Serializable {

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
        if (!(o instanceof CustomerServiceDTO)) {
            return false;
        }

        CustomerServiceDTO customerServiceDTO = (CustomerServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceDTO{" +
            "id='" + getId() + "'" +
            "}";
    }
}
