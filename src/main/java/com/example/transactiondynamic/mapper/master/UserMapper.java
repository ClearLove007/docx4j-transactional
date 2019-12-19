package com.example.transactiondynamic.mapper.master;

import com.example.transactiondynamic.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 18:09 2019/11/29
 */
@Repository("masterUserMapper")
public interface UserMapper {

    List<User> selectAll();

    void insert(@Param("user") User user);
}
