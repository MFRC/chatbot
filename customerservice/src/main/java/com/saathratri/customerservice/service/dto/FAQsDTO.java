package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.FAQs} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FAQsDTO implements Serializable {

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
        if (!(o instanceof FAQsDTO)) {
            return false;
        }

        FAQsDTO fAQsDTO = (FAQsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fAQsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FAQsDTO{" +
            "id='" + getId() + "'" +
            "}";
    }
}
