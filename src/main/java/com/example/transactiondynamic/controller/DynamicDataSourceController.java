package com.example.transactiondynamic.controller;

import com.example.transactiondynamic.entity.User;
import com.example.transactiondynamic.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 18:24 2019/11/29
 */
@RestController
@RequestMapping("/datasource")
@Slf4j
public class DynamicDataSourceController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseEntity<?> insert(@RequestBody User user){
        log.info(user.toString());
        userService.insert(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
