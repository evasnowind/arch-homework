package com.prayerlaputa.week11.encrpyt_password.service;

import com.prayerlaputa.week11.encrpyt_password.mapper.UserMapper;
import com.prayerlaputa.week11.encrpyt_password.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mbeans.UserMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author chenglong.yu
 * created on 2020/12/6
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    BCryptPasswordEncoder bcryptPasswordEncoder;

    public Boolean regUser(String username, String passwd) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(passwd)) {
            return Boolean.FALSE;
        }

        String encrypted = bcryptPasswordEncoder.encode(passwd);
        log.info("username={}, passwd={}, encrypted={}.", username, passwd, encrypted);

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPasswd(encrypted);

        Integer insertCnt = userMapper.insert(userInfo);
        return null != insertCnt && insertCnt > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean login(String username, String passwd) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(passwd)) {
            return Boolean.FALSE;
        }

        //根据用户名查询出已有密码
        UserInfo userInfo = userMapper.select(username);

        //比较两个密码
        return checkPW(username, passwd, userInfo.getPasswd());
    }

    public boolean checkPW(String username, String rawPassword, String encodedPassword) {
        log.info("checkPW: username={}, rawPassword={}, encodedPassword={}.",
                username, rawPassword, encodedPassword);
        return bcryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
