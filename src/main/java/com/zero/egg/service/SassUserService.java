package com.zero.egg.service;

import com.zero.egg.model.SassUser;

/**
 * @description Saas平台用户信息接口
 * @author lym
 */
public interface SassUserService{


//    int deleteByPrimaryKey(String id);

//    int insert(SassUser record);

//    int insertSelective(SassUser record);

    /**
     * 根据主键id查询Sass用户信息
     * @param id
     * @return
     */
    SassUser selectByPrimaryKey(String id);

//    int updateByPrimaryKeySelective(SassUser record);

//    int updateByPrimaryKey(SassUser record);

    /**
     * Sass登录校验
     * @param loginname
     * @param password
     * @return
     */
    SassUser sassLogin(String loginname, String password);

}
