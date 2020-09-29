package com.ithelp.ironman.ecdemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ithelp.ironman.ecdemo.bean.es.UserInfo;
import com.ithelp.ironman.ecdemo.esdao.UserInfoDAO;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ithelp.ironman.ecdemo.constant.EsConstant.INDEX;
import static com.ithelp.ironman.ecdemo.constant.EsConstant.TYPE;

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
}
