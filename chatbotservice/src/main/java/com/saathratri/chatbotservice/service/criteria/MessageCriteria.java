package com.saathratri.chatbotservice.service.criteria;

import com.saathratri.chatbotservice.domain.enumeration.SenderType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.saathratri.chatbotservice.domain.Message} entity. This class is used
 * in {@link com.saathratri.chatbotservice.web.rest.MessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SenderType
     */
    public static class SenderTypeFilter extends Filter<SenderType> {

        public SenderTypeFilter() {}

        public SenderTypeFilter(SenderTypeFilter filter) {
            super(filter);
        }

        @Override
        public SenderTypeFilter copy() {
            return new SenderTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter content;

    private InstantFilter timestamp;

    private SenderTypeFilter senderType;

    private StringFilter userId;

    private UUIDFilter chatSessionId;

    private Boolean distinct;

    public MessageCriteria() {}

    public MessageCriteria(MessageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.timestamp = other.timestamp == null ? null : other.timestamp.copy();
        this.senderType = other.senderType == null ? null : other.senderType.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.chatSessionId = other.chatSessionId == null ? null : other.chatSessionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MessageCriteria copy() {
        return new MessageCriteria(this);
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

    public StringFilter getContent() {
        return content;
    }

    public StringFilter content() {
        if (content == null) {
            content = new StringFilter();
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            timestamp = new InstantFilter();
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public SenderTypeFilter getSenderType() {
        return senderType;
    }

    public SenderTypeFilter senderType() {
        if (senderType == null) {
            senderType = new SenderTypeFilter();
        }
        return senderType;
    }

    public void setSenderType(SenderTypeFilter senderType) {
        this.senderType = senderType;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public UUIDFilter getChatSessionId() {
        return chatSessionId;
    }

    public UUIDFilter chatSessionId() {
        if (chatSessionId == null) {
            chatSessionId = new UUIDFilter();
        }
        return chatSessionId;
    }

    public void setChatSessionId(UUIDFilter chatSessionId) {
        this.chatSessionId = chatSessionId;
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
        final MessageCriteria that = (MessageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(content, that.content) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(senderType, that.senderType) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(chatSessionId, that.chatSessionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, timestamp, senderType, userId, chatSessionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (content != null ? "content=" + content + ", " : "") +
            (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
            (senderType != null ? "senderType=" + senderType + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (chatSessionId != null ? "chatSessionId=" + chatSessionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
