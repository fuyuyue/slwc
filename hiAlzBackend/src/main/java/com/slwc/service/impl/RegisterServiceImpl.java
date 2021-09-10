package com.slwc.service.impl;

import com.slwc.dao.UserInfoMapper;
import com.slwc.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl {
    @Autowired
    UserInfoMapper userInfoMapper;

    public boolean addUser(UserInfoVo userInfoVo) {
        userInfoVo.setRoleManager("N");
        userInfoVo.setStatus("Audit");
        return userInfoMapper.addUser(userInfoVo);
    }
}
