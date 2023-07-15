package com.saathratri.messagesender.service.dto;

import java.io.Serializable;

import com.saathratri.SaathratriBaseDTO;

import lombok.Data;

@Data
public class AuthenticationInfoDTO implements SaathratriBaseDTO, Serializable {
    private String accountPhoneNumber;
    private String accountSid;
    private String accountAuthToken;
}
