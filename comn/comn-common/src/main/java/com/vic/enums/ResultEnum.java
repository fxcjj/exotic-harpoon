package com.vic.enums;

import lombok.Getter;

/**
 *
 * @author victor
 */
@Getter
public enum ResultEnum {
    SUCCESS(200, "成功"),
    FAIL(500, "失败"),
    LOGIN_FAIL(1, "登录失败"),
    ROLE_ERROR(2, "角色有误"),
    REPEAT_SUBMISSION_ERROR(2, "角色有误"),

    PARAM_ERROR(1, "参数错误"),
    CART_EMPTY(2, "购物车为空"),

    PRODUCT_NOT_EXISTS(1, "商品不存在"),
    PRODUCT_STOCK_ERROR(2, "库存有误"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
