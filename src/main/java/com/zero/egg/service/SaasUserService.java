package com.zero.egg.service;

import com.zero.egg.model.SaasUser;

/**
 * @description Saas平台用户信息接口
 * @author lym
 */
public interface SaasUserService{


//    int deleteByPrimaryKey(String id);

//    int insert(SaasUser record);

//    int insertSelective(SaasUser record);

    /**
     * 根据主键id查询Saas用户信息
     * @param id
     * @return
     */
    SaasUser selectByPrimaryKey(String id);

//    int updateByPrimaryKeySelective(SaasUser record);

//    int updateByPrimaryKey(SaasUser record);

    /**
     * Saas登录校验
     * @param loginname
     * @param password
     * @return
     */
    SaasUser saasLogin(String loginname, String password);

}
