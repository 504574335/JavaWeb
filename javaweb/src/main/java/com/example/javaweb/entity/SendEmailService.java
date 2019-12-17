package com.example.javaweb.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
    @Autowired
    JavaMailSender jms;

    public String send(String sender,String receiver,String title,String text){
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送方
        mainMessage.setFrom(sender);
        //接收方
        mainMessage.setTo(receiver);
        //发送的标题
        mainMessage.setSubject(title);
        //发送的内容
        mainMessage.setText(text);
        jms.send(mainMessage);
        return "success";
    }
}
