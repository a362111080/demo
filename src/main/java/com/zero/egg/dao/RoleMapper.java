package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.Resource;
import com.zero.egg.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据普通用户id获取权限列表
     * @param id
     * @return
     */
    List<Resource> selectPermisisonListByUserId(@Param("id") String id);

    /**
     * 根据企业用户id获取权限列表
     * @param id
     * @return
     */
    List<Resource> selectPermisisonListByCompanyUserId(@Param("id") String id);

}