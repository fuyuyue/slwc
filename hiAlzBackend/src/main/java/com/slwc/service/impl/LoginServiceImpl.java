package com.slwc.service.impl;

import com.slwc.dao.UserInfoMapper;
import com.slwc.entity.UserInfoEntity;
import com.slwc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserInfoMapper mapper;

    @Override
    public String verifyPwd(String userName, String pwd) {
        String pwdInDb = mapper.verifyPwd(userName);
        if (!pwd.equals(pwdInDb)) {
            return "error";
        }
        UserInfoEntity entity = mapper.getUserByName(userName);
        return entity.getStatus() + ";" + entity.getRole();
    }
}
