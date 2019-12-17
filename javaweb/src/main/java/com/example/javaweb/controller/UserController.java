package com.example.javaweb.controller;

import com.example.javaweb.entity.User;
import com.example.javaweb.repository.UserRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/addUser")
    public String  insertUser(@ModelAttribute User user, HttpSession session, ModelMap modelMap){
        List<User> users = userRepository.findAll();
        int i = 0;
        while(i<users.size()){
            if(users.get(i).getName().equals(user.getName())){
                modelMap.addAttribute("rename","rename");
                return "register";
            }
            i++;
        }
        userRepository.save(user);
        session.setAttribute("registerSuccess", "注册新用户成功！");
        return "redirect:/login.html";
    }

    @GetMapping("/deleteUser")
    public String delete(Integer id){
        userRepository.deleteById(id);
        return "redirect:/tables.html";
    }
}
