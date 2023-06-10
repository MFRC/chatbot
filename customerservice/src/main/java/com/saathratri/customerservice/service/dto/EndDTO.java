package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.End} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EndDTO implements Serializable {

    @NotNull
    private UUID id;

    private String closeMessage;

    private String moreHelp;

    private ReportDTO report;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCloseMessage() {
        return closeMessage;
    }

    public void setCloseMessage(String closeMessage) {
        this.closeMessage = closeMessage;
    }

    public String getMoreHelp() {
        return moreHelp;
    }

    public void setMoreHelp(String moreHelp) {
        this.moreHelp = moreHelp;
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EndDTO)) {
            return false;
        }

        EndDTO endDTO = (EndDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, endDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EndDTO{" +
            "id='" + getId() + "'" +
            ", closeMessage='" + getCloseMessage() + "'" +
            ", moreHelp='" + getMoreHelp() + "'" +
            ", report=" + getReport() +
            "}";
    }
}
