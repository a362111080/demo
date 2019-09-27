package com.zero.egg.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.CompanyUserMapper;
import com.zero.egg.dao.RoleMapper;
import com.zero.egg.dao.UserRoleMapper;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.Resource;
import com.zero.egg.model.Role;
import com.zero.egg.security.user.SecurityUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @ClassName WxbindUserDetailsServiceImpl
 * @Author lyming
 * @Date 2019/9/26 8:50 上午
 **/
public class WxbindUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CompanyUserMapper companyUserMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //企业用户
        CompanyUser companyUser = companyUserMapper.selectOne(new QueryWrapper<CompanyUser>().eq("loginname", username));
        List<Resource> resources = roleMapper.selectPermisisonListByCompanyUserId(companyUser.getId());
        List<Role> roles = userRoleMapper.selectRolesByCompanyUserId(companyUser.getId());
        companyUser.setRoles(roles);
        companyUser.setResources(resources);
        return new SecurityUserDetails(companyUser);
    }
}
