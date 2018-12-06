package com.zero.egg.service;

import com.zero.egg.model.LoginInfo;

public interface SystemService {
    //登录验证
    LoginInfo  checklogin(String strUserName,String passWord);
}
