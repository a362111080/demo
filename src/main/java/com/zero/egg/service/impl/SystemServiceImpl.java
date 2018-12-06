package com.zero.egg.service.impl;

import com.zero.egg.dao.SystemMapper;
import com.zero.egg.model.LoginInfo;
import com.zero.egg.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SystemMapper mapper;
    @Override
    public  LoginInfo checklogin(String strUserName,String passWord)
    {
        return  mapper.checklogin(strUserName,passWord);
    }
}
