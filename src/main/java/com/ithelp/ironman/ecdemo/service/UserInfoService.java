package com.ithelp.ironman.ecdemo.service;

import com.ithelp.ironman.ecdemo.bean.es.UserInfo;
import com.ithelp.ironman.ecdemo.esdao.UserInfoDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
public class UserInfoService {
    @Resource
    private UserInfoDAO userInfoDAO;


    public String createDoc(UserInfo userInfo) throws IOException {
        UUID uuid = UUID.randomUUID();
        userInfo.setId(uuid.toString());
        return userInfoDAO.createDoc(userInfo).getResult().name();
    }

    public List<UserInfo> findAll() throws IOException {
        return userInfoDAO.findAll();
    }

    public List<UserInfo> findUserInfoByField(String field, String name) throws IOException {
        return userInfoDAO.findByName(field,name);
    }

    public UserInfo findById(String id) throws IOException {
        return userInfoDAO.findById(id);
    }

    public String updateDoc(UserInfo userInfo) throws Exception {
        return userInfoDAO.updateDoc(userInfo);
    }

    public String deleteUserInfo(String id) throws IOException {
        return userInfoDAO.deleteDoc(id);
    }
}
