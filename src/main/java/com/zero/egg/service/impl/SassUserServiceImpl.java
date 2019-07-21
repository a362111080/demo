package com.zero.egg.service.impl;

import com.zero.egg.dao.SassUserMapper;
import com.zero.egg.model.SassUser;
import com.zero.egg.service.SassUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class SassUserServiceImpl implements SassUserService{

    @Resource
    private SassUserMapper sassUserMapper;

    @Override
    public SassUser sassLogin(String loginname, String password) {
        return sassUserMapper.findOneByLoginnameAndPassword(loginname,password);
    }

//    @Override
//    public int deleteByPrimaryKey(String id) {
//        return sassUserMapper.deleteByPrimaryKey(id);
//    }

//    @Override
//    public int insert(SassUser record) {
//        return sassUserMapper.insert(record);
//    }

//    @Override
//    public int insertSelective(SassUser record) {
//        return sassUserMapper.insertSelective(record);
//    }

    @Override
    public SassUser selectByPrimaryKey(String id) {
        return sassUserMapper.selectByPrimaryKey(id);
    }

//    @Override
//    public int updateByPrimaryKeySelective(SassUser record) {
//        return sassUserMapper.updateByPrimaryKeySelective(record);
//    }

//    @Override
//    public int updateByPrimaryKey(SassUser record) {
//        return sassUserMapper.updateByPrimaryKey(record);
//    }

}
