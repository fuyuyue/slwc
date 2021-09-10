package com.slwc.vo;

import org.jetbrains.annotations.NotNull;
import lombok.Data;

@Data
public class UserInfoVo {

    @NotNull
    private String id;

    private String userName;

    private String pwd;

    private String role;

    private String roleManager;

    private String status;

    private String realName;

    private String mail;

    private String phone;
}
