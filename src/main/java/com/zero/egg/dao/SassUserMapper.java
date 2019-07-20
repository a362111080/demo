package com.zero.egg.dao;

import com.zero.egg.model.SassUser;
import org.apache.ibatis.annotations.Param;

public interface SassUserMapper {
//    int deleteByPrimaryKey(String id);

//    int insert(SassUser record);

//    int insertSelective(SassUser record);

    SassUser selectByPrimaryKey(String id);

//    int updateByPrimaryKeySelective(SassUser record);

//    int updateByPrimaryKey(SassUser record);

    /**
     * 根据账号密码找到SassUser信息
     * @param loginname
     * @param password
     * @return
     */
    SassUser findOneByLoginnameAndPassword(@Param("loginname")String loginname,@Param("password")String password);



}