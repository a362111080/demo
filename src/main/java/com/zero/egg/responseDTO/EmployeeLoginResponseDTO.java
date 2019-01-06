package com.zero.egg.responseDTO;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeLoginResponseDTO {

    // 性别
    private Integer strUserSex;
    // 手机
    private String strUserPhone;
    // 用户编码
    private String strUserCode;
    // 姓名
    private String strUserName;
    // 登录密码
    private String strPassWord;
    // 店铺编码
    private String strstorecode;
    // 入职时间
    private String strentrytime;
    // 职称
    private String strprofession;
    // 店铺名称
    private String strstorename;
    // 店铺地址
    private String strstoreaddress;
    // 激活码有效期截止日期
    private String validitydata;
}
