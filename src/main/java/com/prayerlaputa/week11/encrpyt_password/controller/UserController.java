package com.prayerlaputa.week11.encrpyt_password.controller;

import com.prayerlaputa.week11.encrpyt_password.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author chenglong.yu
 * created on 2020/12/6
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/reg")
    public Boolean registerUser(String username, String passwd) {
        return userService.regUser(username, passwd);
    }

    @GetMapping("/login")
    public Boolean login(String username, String passwd) {
        return userService.login(username, passwd);
    }
}
