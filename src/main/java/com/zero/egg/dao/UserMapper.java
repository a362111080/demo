package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.User;
import com.zero.egg.requestDTO.UserRequest;
import com.zero.egg.responseDTO.UserListResponseDTO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
public interface UserMapper extends BaseMapper<User> {

    User getUserinfo(User user);

    /**
     * 获取普通账号信息
     * @param userRequest
     * @return
     */
    List<UserListResponseDTO> getUserList(UserRequest userRequest);

}
