package com.vic.enums.order;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    NEW(1, "新订单"),
    FINISHED(3, "完结"),
    CANCEL(5, "取消"),
    ;
    private Integer code;

    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
