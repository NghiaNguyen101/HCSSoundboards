package com.hcs.soundboard.controller;

import com.hcs.soundboard.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 * Created by nnguy101 on 12/5/16.
 */
@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("/sendMail")
    @ResponseBody
    public String sendMail(){
        try {
            emailService.sendEmail();
        } catch (MailException e){
            System.out.println("Failed to send mail");
        }
        return "Sent";
    }
}
