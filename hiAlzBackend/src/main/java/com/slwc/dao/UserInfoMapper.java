package com.slwc.dao;

import com.slwc.entity.UserInfoEntity;
import com.slwc.vo.UserInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoMapper {
    int existUser(String userName);

    String verifyPwd(String userName);

    boolean addUser(UserInfoVo userInfoVo);

    UserInfoEntity getUserByName(String userName);

    List<UserInfoVo> getRegisterList();

    void approve(String id);

    void deleteById(String id);

    void updatePwd(String newPwd, String userName);
}
