package com.example.transactiondynamic.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 18:19 2019/11/29
 */
@Getter
@Setter
public class User {

    private Long id;
    private String name;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
