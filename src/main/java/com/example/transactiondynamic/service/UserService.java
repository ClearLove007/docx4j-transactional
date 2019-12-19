package com.example.transactiondynamic.service;

import com.example.transactiondynamic.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 15:56 2019/12/5
 */
@Service
@Slf4j
public class UserService {


    @Autowired
    @Qualifier("masterUserMapper")
    private com.example.transactiondynamic.mapper.master.UserMapper masterUserMapper;

    @Autowired
    @Qualifier("severntUserMapper")
    private com.example.transactiondynamic.mapper.severnt.UserMapper severntUserMapper;


    @Transactional(value = "platformTransactionManager")
    public void insert(User user){
        severntUserMapper.insert(user);
        masterUserMapper.insert(user);
    }

}
