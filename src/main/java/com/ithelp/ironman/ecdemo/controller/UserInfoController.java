package com.ithelp.ironman.ecdemo.controller;

import com.ithelp.ironman.ecdemo.bean.es.UserInfo;
import com.ithelp.ironman.ecdemo.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @PostMapping
    public ResponseEntity createProfile(@RequestBody UserInfo userInfo) throws Exception {
        return new ResponseEntity(userInfoService.createDoc(userInfo), HttpStatus.CREATED);
    }

    @GetMapping
    public List<UserInfo> findAll() throws Exception {
        return userInfoService.findAll();
    }

    @GetMapping(value = "/search/{field}/{value}")
    public List<UserInfo> searchByField(@PathVariable String field, @PathVariable String value) throws Exception {
        return userInfoService.findUserInfoByField(field,value);
    }
}
