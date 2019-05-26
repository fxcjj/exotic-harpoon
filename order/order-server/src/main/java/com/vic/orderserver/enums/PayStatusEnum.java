package com.vic.orderserver.enums;

import lombok.Getter;


@Getter
public enum PayStatusEnum {
    WAIT(1, "等待支付"),
    SUCCESS(3, "支付成功"),
    ;
    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
