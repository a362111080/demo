package com.zero.egg.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.RoleMapper;
import com.zero.egg.dao.UserMapper;
import com.zero.egg.dao.UserRoleMapper;
import com.zero.egg.model.Company;
import com.zero.egg.model.Resource;
import com.zero.egg.model.Role;
import com.zero.egg.model.User;
import com.zero.egg.security.user.SecurityUserDetails;
import com.zero.egg.service.ICompanyService;
import com.zero.egg.tool.AuthenticateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName UserDetailsServiceImpl
 * @Author lyming
 * @Date 2019/9/26 8:10 上午
 **/
@Component
public class NomalUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private ICompanyService iCompanyService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //普通用户
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("loginname", username)
                .eq("dr", false));
        if (user == null) {
            throw new AuthenticateException(401, "账号不存在");
        }
        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        companyQueryWrapper.eq("id", user.getCompanyId()).eq("dr", false);
        //验证企业是否过期
        Company company = iCompanyService.getOne(companyQueryWrapper);
        if (company == null) {
            throw new AuthenticateException(401, "账号已过期");
        }
        List<Resource> resources = roleMapper.selectPermisisonListByUserId(user.getId());
        List<Role> roles = userRoleMapper.selectRolesByUserId(user.getId());
        user.setResources(resources);
        user.setRoles(roles);
        return new SecurityUserDetails(user);
    }
}
