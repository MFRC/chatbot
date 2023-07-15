package com.saathratri.messagesender.service.dto;

import java.io.Serializable;

import com.saathratri.SaathratriBaseDTO;

import lombok.Data;

@Data
public class ContactUsMessageDTO implements SaathratriBaseDTO, Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;

    private String emailAddress;

    private String phoneNumber;

    private String message;
}
