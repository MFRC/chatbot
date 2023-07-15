package com.saathratri.messagesender.service.dto;

import java.io.Serializable;

import com.saathratri.SaathratriBaseDTO;

import lombok.Data;

@Data
public class EmailMessageDTO implements SaathratriBaseDTO, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String from;
    private String to;
    private String tos;
    private String subject;
    private String content;
    private String userLanguageKey;
}
