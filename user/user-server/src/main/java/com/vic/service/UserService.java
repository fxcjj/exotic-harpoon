package com.vic.service;


import com.vic.dataobject.UserInfo;

public interface UserService {
    UserInfo findByOpenid(String openid);
}
