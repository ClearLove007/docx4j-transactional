package com.example.transactiondynamic.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 17:00 2019/12/9
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.master")
public class MasterDB {
    private String url;
    private String username;
    private String password;
}
