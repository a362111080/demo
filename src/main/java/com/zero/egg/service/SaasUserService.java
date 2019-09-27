package com.zero.egg.service;

import com.zero.egg.model.SaasUser;

/**
 * @description Saas平台用户信息接口
 * @author lym
 */
public interface SaasUserService{

    /**
     * 根据主键id查询SaasUser用户信息
     * @param id
     * @return
     */
    SaasUser selectByPrimaryKey(String id);

    /**
     * SaasUser登录校验
     * @param loginname
     * @param password
     * @return
     */
    SaasUser saasLogin(String loginname, String password);

}
