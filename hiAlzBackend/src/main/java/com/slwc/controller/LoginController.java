package com.slwc.controller;

import com.slwc.service.impl.LoginServiceImpl;
import com.slwc.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    LoginServiceImpl loginService;

    @PostMapping("/login")
    public String login(@RequestBody UserInfoVo userInfoVo) {
        System.out.println("welcome:" + userInfoVo.getUserName());
        return loginService.verifyPwd(userInfoVo.getUserName(), userInfoVo.getPwd());
    }
}
