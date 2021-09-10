package com.slwc.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class UserInfoEntity {
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
