package com.slwc.service.impl;

import com.slwc.dao.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    public boolean existUser(String userName) {
        return userInfoMapper.existUser(userName) > 0;
    }

    @Transactional
    public void setRoleManager(String id) {
        userInfoMapper.setNotRoleManager();
        userInfoMapper.setRoleManager(id);
    }
}
