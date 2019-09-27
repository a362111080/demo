package com.zero.egg.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.RoleMapper;
import com.zero.egg.dao.SaasUserMapper;
import com.zero.egg.dao.UserRoleMapper;
import com.zero.egg.model.Resource;
import com.zero.egg.model.Role;
import com.zero.egg.model.SaasUser;
import com.zero.egg.security.user.SecurityUserDetails;
import com.zero.egg.tool.AuthenticateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName SaasUserDetailsServiceImpl
 * @Author lyming
 * @Date 2019/9/27 4:50 下午
 **/
@Component
public class SaasUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private SaasUserMapper saasUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SaasUser saasUser = saasUserMapper.selectOne(new QueryWrapper<SaasUser>()
                .eq("loginname", username));
        if (saasUser == null) {
            throw new AuthenticateException(401, "账号不存在");
        }
        List<Resource> resources = roleMapper.selectPermisisonListByUserId(saasUser.getId());
        List<Role> roles = userRoleMapper.selectRolesByUserId(saasUser.getId());
        saasUser.setResources(resources);
        saasUser.setRoles(roles);
        return new SecurityUserDetails(saasUser);
    }
}
