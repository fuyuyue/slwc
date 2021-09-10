package com.slwc.service.impl;

import com.slwc.dao.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    public boolean existUser(String userName) {
        return userInfoMapper.existUser(userName) > 0;
    }
}
