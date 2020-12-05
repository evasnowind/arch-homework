package com.prayerlaputa.week11.encrpyt_password.mapper;

import com.prayerlaputa.week11.encrpyt_password.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chenglong.yu
 * created on 2020/12/6
 */
@Mapper
public interface UserMapper {

    UserInfo select(@Param("username") String username);

    Integer insert(UserInfo userInfo);
}
