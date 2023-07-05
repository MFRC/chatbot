package com.saathratri.customerservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.customerservice.domain.Report} entity. This class is used
 * in {@link com.saathratri.customerservice.web.rest.ReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private InstantFilter time;

    private LongFilter reportNumber;

    private StringFilter moreHelp;

    private IntegerFilter satisfaction;

    private UUIDFilter endId;

    private Boolean distinct;

    public ReportCriteria() {}

    public ReportCriteria(ReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.reportNumber = other.reportNumber == null ? null : other.reportNumber.copy();
        this.moreHelp = other.moreHelp == null ? null : other.moreHelp.copy();
        this.satisfaction = other.satisfaction == null ? null : other.satisfaction.copy();
        this.endId = other.endId == null ? null : other.endId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportCriteria copy() {
        return new ReportCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public InstantFilter getTime() {
        return time;
    }

    public InstantFilter time() {
        if (time == null) {
            time = new InstantFilter();
        }
        return time;
    }

    public void setTime(InstantFilter time) {
        this.time = time;
    }

    public LongFilter getReportNumber() {
        return reportNumber;
    }

    public LongFilter reportNumber() {
        if (reportNumber == null) {
            reportNumber = new LongFilter();
        }
        return reportNumber;
    }

    public void setReportNumber(LongFilter reportNumber) {
        this.reportNumber = reportNumber;
    }

    public StringFilter getMoreHelp() {
        return moreHelp;
    }

    public StringFilter moreHelp() {
        if (moreHelp == null) {
            moreHelp = new StringFilter();
        }
        return moreHelp;
    }

    public void setMoreHelp(StringFilter moreHelp) {
        this.moreHelp = moreHelp;
    }

    public IntegerFilter getSatisfaction() {
        return satisfaction;
    }

    public IntegerFilter satisfaction() {
        if (satisfaction == null) {
            satisfaction = new IntegerFilter();
        }
        return satisfaction;
    }

    public void setSatisfaction(IntegerFilter satisfaction) {
        this.satisfaction = satisfaction;
    }

    public UUIDFilter getEndId() {
        return endId;
    }

    public UUIDFilter endId() {
        if (endId == null) {
            endId = new UUIDFilter();
        }
        return endId;
    }

    public void setEndId(UUIDFilter endId) {
        this.endId = endId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReportCriteria that = (ReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(time, that.time) &&
            Objects.equals(reportNumber, that.reportNumber) &&
            Objects.equals(moreHelp, that.moreHelp) &&
            Objects.equals(satisfaction, that.satisfaction) &&
            Objects.equals(endId, that.endId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, reportNumber, moreHelp, satisfaction, endId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (time != null ? "time=" + time + ", " : "") +
            (reportNumber != null ? "reportNumber=" + reportNumber + ", " : "") +
            (moreHelp != null ? "moreHelp=" + moreHelp + ", " : "") +
            (satisfaction != null ? "satisfaction=" + satisfaction + ", " : "") +
            (endId != null ? "endId=" + endId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
