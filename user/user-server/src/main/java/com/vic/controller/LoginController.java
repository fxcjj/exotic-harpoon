package com.vic.controller;

import com.vic.constants.CookieConstants;
import com.vic.entity.user.UserInfo;
import com.vic.enums.ResultEnum;
import com.vic.enums.RoleEnum;
import com.vic.service.UserService;
import com.vic.utils.CookieUtils;
import com.vic.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping("buyer")
    public ResultVo buyer(@RequestParam("openid") String openid, HttpServletResponse response) {
        //1. 判断用户是否存在
        UserInfo userInfo = userService.findByOpenid(openid);
        if(userInfo == null) {
            return ResultVo.fail(ResultEnum.LOGIN_FAIL);
        }

        if(!RoleEnum.BUYER.getCode().equals(userInfo.getRole())) {
            return ResultVo.fail(ResultEnum.ROLE_ERROR);
        }

        CookieUtils.set(response, CookieConstants.OPENID, openid, CookieConstants.EXPIRE);

        return ResultVo.success();
    }


}
