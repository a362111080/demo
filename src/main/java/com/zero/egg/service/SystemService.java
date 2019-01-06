package com.zero.egg.service;

import com.zero.egg.model.LoginInfo;
import com.zero.egg.responseDTO.EmployeeLoginResponseDTO;

public interface SystemService {
    //登录验证
    LoginInfo  checklogin(String strUserName,String passWord);

    EmployeeLoginResponseDTO Mobilelogin(String strPassName, String strPassword);
}
