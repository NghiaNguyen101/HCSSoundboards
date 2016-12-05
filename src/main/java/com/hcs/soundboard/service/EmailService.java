package com.hcs.soundboard.service;

/**
 * Created by nnguy101 on 12/5/16.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail() throws MailException{
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("nnguy101@gmail.com");
        mail.setFrom("nnguy101@gmail.com");
        mail.setSubject("Test send mail in spring");
        mail.setText("Please work");

        javaMailSender.send(mail);
    }
}
