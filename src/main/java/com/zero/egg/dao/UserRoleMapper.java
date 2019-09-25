package com.zero.egg.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.Role;
import com.zero.egg.model.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    int deleteByPrimaryKey(String id);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserRole record);

    /**
     * 根据普通用户id查询所有角色权限
     * @param id
     * @return
     */
    List<Role> selectRolesByUserId(@Param("id") String id);

    List<Role> selectRolesByCompanyUserId(@Param("id") String id);
}