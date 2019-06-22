package com.vic.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class UserInfo {

    @Id
    private String id;

    private String username;
    private String password;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 1买家2卖家
     */
    private Integer role;

}