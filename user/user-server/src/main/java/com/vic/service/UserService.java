package com.vic.service;


import com.vic.entity.UserInfo;

public interface UserService {
    UserInfo findByOpenid(String openid);
}
