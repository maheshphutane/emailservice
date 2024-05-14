package com.example.emailserviceapi.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SendEmailDTO implements Serializable {
    private String emailTo;
    private String emailFrom;
    private String subject;
    private String emailBody;
}
