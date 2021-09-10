package com.slwc.controller;

import com.slwc.service.impl.RegisterServiceImpl;
import com.slwc.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class RegisterController {
    @Autowired
    RegisterServiceImpl registerService;

    @PostMapping("/register")
    public boolean addUser(@RequestBody UserInfoVo userInfoVo) {
        return registerService.addUser(userInfoVo);
    }
}
