package com.saathratri.customerservice.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.saathratri.customerservice.domain.Report} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDTO implements Serializable {

  private Long id;

  private ZonedDateTime time;

  private Long reportNumber;

  private String moreHelp;

  private Integer satisfaction;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ZonedDateTime getTime() {
    return time;
  }

  public void setTime(ZonedDateTime time) {
    this.time = time;
  }

  public Long getReportNumber() {
    return reportNumber;
  }

  public void setReportNumber(Long reportNumber) {
    this.reportNumber = reportNumber;
  }

  public String getMoreHelp() {
    return moreHelp;
  }

  public void setMoreHelp(String moreHelp) {
    this.moreHelp = moreHelp;
  }

  public Integer getSatisfaction() {
    return satisfaction;
  }

  public void setSatisfaction(Integer satisfaction) {
    this.satisfaction = satisfaction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ReportDTO)) {
      return false;
    }

    ReportDTO reportDTO = (ReportDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, reportDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "ReportDTO{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", reportNumber=" + getReportNumber() +
            ", moreHelp='" + getMoreHelp() + "'" +
            ", satisfaction=" + getSatisfaction() +
            "}";
    }
}
