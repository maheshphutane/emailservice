package com.example.emailserviceapi.consumers;

import com.example.emailserviceapi.dtos.SendEmailDTO;
import com.example.emailserviceapi.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEmailConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "send-email", groupId = "emailService")
    public void sendEmail(String sendEmailDTOString) {
        try {
            SendEmailDTO sendEmailDTO = objectMapper.readValue(sendEmailDTOString, SendEmailDTO.class);

            // Extract the emailTo, emailFrom, subject, and emailBody from sendEmailDTO
            String emailTo = sendEmailDTO.getEmailTo();
            String emailFrom = sendEmailDTO.getEmailFrom();
            String subject = sendEmailDTO.getSubject();
            String emailBody = sendEmailDTO.getEmailBody();

            // Send the email

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailFrom, "app_password");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, emailTo, subject, emailBody);
            System.out.println("MAil Sent to " + emailTo);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing sendEmailDTOString", e);
        }catch (Exception e){
            throw new RuntimeException("Error sending email", e);
        }
    }
}
