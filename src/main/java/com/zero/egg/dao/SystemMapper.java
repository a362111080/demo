package com.zero.egg.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SystemMapper {
    int checklogin(String strUserName,String passWord);
}
