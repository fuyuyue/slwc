package com.slwc.controller;

import com.slwc.service.impl.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/existUser")
    public boolean existUser(@RequestParam(value = "userName") String userName) {
        return userInfoService.existUser(userName);
    }
}
