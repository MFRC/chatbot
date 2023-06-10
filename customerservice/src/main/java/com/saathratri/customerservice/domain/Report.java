package com.saathratri.customerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "time")
    private Instant time;

    @Column(name = "report_number")
    private Long reportNumber;

    @Column(name = "more_help")
    private String moreHelp;

    @Column(name = "satisfaction")
    private Integer satisfaction;

    @JsonIgnoreProperties(value = { "report", "conversation" }, allowSetters = true)
    @OneToOne(mappedBy = "report")
    private End end;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Report id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getTime() {
        return this.time;
    }

    public Report time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Long getReportNumber() {
        return this.reportNumber;
    }

    public Report reportNumber(Long reportNumber) {
        this.setReportNumber(reportNumber);
        return this;
    }

    public void setReportNumber(Long reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getMoreHelp() {
        return this.moreHelp;
    }

    public Report moreHelp(String moreHelp) {
        this.setMoreHelp(moreHelp);
        return this;
    }

    public void setMoreHelp(String moreHelp) {
        this.moreHelp = moreHelp;
    }

    public Integer getSatisfaction() {
        return this.satisfaction;
    }

    public Report satisfaction(Integer satisfaction) {
        this.setSatisfaction(satisfaction);
        return this;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public End getEnd() {
        return this.end;
    }

    public void setEnd(End end) {
        if (this.end != null) {
            this.end.setReport(null);
        }
        if (end != null) {
            end.setReport(this);
        }
        this.end = end;
    }

    public Report end(End end) {
        this.setEnd(end);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return id != null && id.equals(((Report) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", reportNumber=" + getReportNumber() +
            ", moreHelp='" + getMoreHelp() + "'" +
            ", satisfaction=" + getSatisfaction() +
            "}";
    }
}
