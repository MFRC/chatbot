package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.CustomerServiceEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerServiceEntityDTO implements Serializable {

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
        if (!(o instanceof CustomerServiceEntityDTO)) {
            return false;
        }

        CustomerServiceEntityDTO customerServiceEntityDTO = (CustomerServiceEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerServiceEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerServiceEntityDTO{" +
            "id='" + getId() + "'" +
            "}";
    }
}
