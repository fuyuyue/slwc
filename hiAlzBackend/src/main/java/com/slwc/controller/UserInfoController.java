package com.slwc.controller;

import com.slwc.service.impl.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/existUser")
    public boolean existUser(@RequestParam(value = "userName") String userName) {
        return userInfoService.existUser(userName);
    }

    @PostMapping("/set-role-manager")
    public boolean setRoleManager(@RequestBody String id) {
        userInfoService.setRoleManager(id);
        return true;
    }
}
