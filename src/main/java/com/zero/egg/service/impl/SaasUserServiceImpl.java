package com.zero.egg.service.impl;

import com.zero.egg.dao.SaasUserMapper;
import com.zero.egg.model.SaasUser;
import com.zero.egg.service.SaasUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class SaasUserServiceImpl implements SaasUserService {

    @Resource
    private SaasUserMapper saasUserMapper;

    @Override
    public SaasUser saasLogin(String loginname, String password) {
        return saasUserMapper.findOneByLoginnameAndPassword(loginname,password);
    }

    @Override
    public SaasUser selectByPrimaryKey(String id) {
        return saasUserMapper.selectByPrimaryKey(id);
    }

}
