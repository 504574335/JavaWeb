package com.example.javaweb.controller;

import com.example.javaweb.entity.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mail {
    @Autowired
    private SendEmailService sendEmailService;

    @GetMapping("/send")
    public String send(){
        String sender="504574335@qq.com";   //这个是发送人的邮箱
        String receiver="1353813139@qq.com";  //这个是接受人的邮箱
        String title="TEST";    //标题
        String text="SUCCESS";
        String result=sendEmailService.send(sender, receiver, title, text);
        return result;
    }
}
