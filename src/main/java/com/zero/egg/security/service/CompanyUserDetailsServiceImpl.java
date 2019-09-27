package com.zero.egg.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.CompanyUserMapper;
import com.zero.egg.dao.RoleMapper;
import com.zero.egg.dao.UserRoleMapper;
import com.zero.egg.model.Company;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.Resource;
import com.zero.egg.model.Role;
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
 * @ClassName CompanyUserDetailsServiceImpl
 * @Description 企业用户
 * @Author lyming
 * @Date 2019/9/27 2:30 下午
 **/
@Component
public class CompanyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CompanyUserMapper companyUserMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private ICompanyService iCompanyService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CompanyUser companyUser = companyUserMapper.selectOne(new QueryWrapper<CompanyUser>()
                .eq("loginname", username)
                .eq("dr", false));
        if (companyUser == null) {
            throw new AuthenticateException(401, "账号不存在");
        }
        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        companyQueryWrapper.eq("id", companyUser.getCompanyId()).eq("dr", false);
        //验证企业是否过期
        Company company = iCompanyService.getOne(companyQueryWrapper);
        if (company == null) {
            throw new AuthenticateException(401, "账号已过期");
        }
        List<Resource> resources = roleMapper.selectPermisisonListByUserId(companyUser.getId());
        List<Role> roles = userRoleMapper.selectRolesByUserId(companyUser.getId());
        companyUser.setResources(resources);
        companyUser.setRoles(roles);
        return new SecurityUserDetails(companyUser);
    }
}
