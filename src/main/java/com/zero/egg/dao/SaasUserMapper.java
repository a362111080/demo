package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.SaasUser;
import org.apache.ibatis.annotations.Param;

public interface SaasUserMapper extends BaseMapper<SaasUser> {
//    int deleteByPrimaryKey(String id);

//    int insert(SaasUser record);

//    int insertSelective(SaasUser record);

    SaasUser selectByPrimaryKey(String id);

//    int updateByPrimaryKeySelective(SaasUser record);

//    int updateByPrimaryKey(SaasUser record);

    /**
     * 根据账号密码找到SaasUser信息
     * @param loginname
     * @param password
     * @return
     */
    SaasUser findOneByLoginnameAndPassword(@Param("loginname")String loginname,@Param("password")String password);



}