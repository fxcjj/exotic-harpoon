package com.vic.entity.product;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class ProductCategory {

    @Id
    @GeneratedValue
    private Integer categoryId;

    //类目名字
    private String categoryName;

    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    public static void main(String[] args) {
        String h = "aa";
        System.out.println(t(h));
    }

    public static int t(Object key) {
        int h = 567890;
        return (key == null) ? 0 : (h >>> 16);
    }
}
