package com.zero.egg.security.user;

import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.CompanyUser;
import com.zero.egg.model.Resource;
import com.zero.egg.model.Role;
import com.zero.egg.model.SaasUser;
import com.zero.egg.model.User;
import com.zero.egg.model.WechatAuth;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName SecurityUserDetails
 * @Description 通用user
 * @Author lyming
 * @Date 2019/9/26 6:58 上午
 **/
@Data
public class SecurityUserDetails implements UserDetails {

    private String id;

    private Integer type;

    //UserDetails带的name字段
    private String name;

    //登录账号
    private String loginname;

    //用户名
    private String username;

    private String password;

    private String shopId;

    private String companyId;

    private Boolean dr;

    private String status;

    private List<Role> roles;

    private List<Resource> resources;

    public SecurityUserDetails(User user) {
        if (user != null) {
            this.setType(user.getType());
            this.setCompanyId(user.getCompanyId());
            this.setShopId(user.getShopId());
            this.setStatus(user.getStatus());
            this.setResources(user.getResources());
            this.setRoles(user.getRoles());
            this.setId(user.getId());
            this.setName(user.getName());
            this.setLoginname(user.getLoginname());
            this.setPassword(user.getPassword());
            this.setDr(user.getDr());
        }
    }


    public SecurityUserDetails(CompanyUser companyUser) {
        if (companyUser != null) {
            this.setType(UserEnums.Type.Company.index());
            this.setCompanyId(companyUser.getCompanyId());
            this.setStatus(companyUser.getStatus());
            this.setResources(companyUser.getResources());
            this.setRoles(companyUser.getRoles());
            this.setId(companyUser.getId());
            this.setName(companyUser.getName());
            this.setLoginname(companyUser.getLoginname());
            this.setPassword(companyUser.getPassword());
            this.setDr(companyUser.getDr());
        }
    }

    public SecurityUserDetails(SaasUser saasUser) {
        if (saasUser != null) {
            this.setType(UserEnums.Type.Company.index());
            this.setResources(saasUser.getResources());
            this.setRoles(saasUser.getRoles());
            this.setId(saasUser.getId());
            this.setLoginname(saasUser.getLoginname());
            this.setPassword(saasUser.getPassword());
        }
    }

    public SecurityUserDetails(WechatAuth wechatAuth) {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        List<Role> roles = this.getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                authorityList.add(new SimpleGrantedAuthority(role.getName()));
            }
        }
        return authorityList;
    }


    /**
     * 账户是否过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
