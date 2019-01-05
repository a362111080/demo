package com.zero.egg.dao;

import com.zero.egg.responseDTO.EmployeeLoginResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.zero.egg.model.LoginInfo;

@Mapper
@Repository
public interface SystemMapper {
    LoginInfo checklogin(String strUserName,String passWord);

    EmployeeLoginResponseDTO Mobilelogin(String strPassName, String strPassword);
}
