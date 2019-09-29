package com.zero.egg.service.impl;

import com.zero.egg.dao.SaasUserMapper;
import com.zero.egg.model.SaasUser;
import com.zero.egg.service.SaasUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class SaasUserServiceImpl implements SaasUserService{

    @Resource
    private SaasUserMapper saasUserMapper;

    @Override
    public SaasUser saasLogin(String loginname, String password) {
        return saasUserMapper.findOneByLoginnameAndPassword(loginname,password);
    }

//    @Override
//    public int deleteByPrimaryKey(String id) {
//        return saasUserMapper.deleteByPrimaryKey(id);
//    }

//    @Override
//    public int insert(SaasUser record) {
//        return saasUserMapper.insert(record);
//    }

//    @Override
//    public int insertSelective(SaasUser record) {
//        return saasUserMapper.insertSelective(record);
//    }

    @Override
    public SaasUser selectByPrimaryKey(String id) {
        return saasUserMapper.selectByPrimaryKey(id);
    }

//    @Override
//    public int updateByPrimaryKeySelective(SaasUser record) {
//        return saasUserMapper.updateByPrimaryKeySelective(record);
//    }

//    @Override
//    public int updateByPrimaryKey(SaasUser record) {
//        return saasUserMapper.updateByPrimaryKey(record);
//    }

}
