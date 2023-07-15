package com.saathratri.messagesender.service.dto;

import java.io.Serializable;

import com.saathratri.SaathratriBaseDTO;

import lombok.Data;

@Data
public class WhatsAppMessageDTO implements SaathratriBaseDTO, Serializable {
    private static final long serialVersionUID = 1L;

    private String toNumber;
    private String fromNumber;
    private String content;

    public WhatsAppMessageDTO() {}
}
