package com.saathratri.customerservice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.customerservice.domain.End} entity. This class is used
 * in {@link com.saathratri.customerservice.web.rest.EndResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ends?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EndCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter closeMessage;

    private StringFilter moreHelp;

    private UUIDFilter reportId;

    private UUIDFilter conversationId;

    private Boolean distinct;

    public EndCriteria() {}

    public EndCriteria(EndCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.closeMessage = other.closeMessage == null ? null : other.closeMessage.copy();
        this.moreHelp = other.moreHelp == null ? null : other.moreHelp.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.conversationId = other.conversationId == null ? null : other.conversationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EndCriteria copy() {
        return new EndCriteria(this);
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

    public StringFilter getCloseMessage() {
        return closeMessage;
    }

    public StringFilter closeMessage() {
        if (closeMessage == null) {
            closeMessage = new StringFilter();
        }
        return closeMessage;
    }

    public void setCloseMessage(StringFilter closeMessage) {
        this.closeMessage = closeMessage;
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

    public UUIDFilter getReportId() {
        return reportId;
    }

    public UUIDFilter reportId() {
        if (reportId == null) {
            reportId = new UUIDFilter();
        }
        return reportId;
    }

    public void setReportId(UUIDFilter reportId) {
        this.reportId = reportId;
    }

    public UUIDFilter getConversationId() {
        return conversationId;
    }

    public UUIDFilter conversationId() {
        if (conversationId == null) {
            conversationId = new UUIDFilter();
        }
        return conversationId;
    }

    public void setConversationId(UUIDFilter conversationId) {
        this.conversationId = conversationId;
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
        final EndCriteria that = (EndCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(closeMessage, that.closeMessage) &&
            Objects.equals(moreHelp, that.moreHelp) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(conversationId, that.conversationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, closeMessage, moreHelp, reportId, conversationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EndCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (closeMessage != null ? "closeMessage=" + closeMessage + ", " : "") +
            (moreHelp != null ? "moreHelp=" + moreHelp + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            (conversationId != null ? "conversationId=" + conversationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
