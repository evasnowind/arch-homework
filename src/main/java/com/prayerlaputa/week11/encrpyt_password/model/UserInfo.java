package com.prayerlaputa.week11.encrpyt_password.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenglong.yu
 * created on 2020/12/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private Integer id;
    private String username;
    private String passwd;
}
