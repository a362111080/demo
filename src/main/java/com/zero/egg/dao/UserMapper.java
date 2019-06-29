package com.zero.egg.dao;

import com.zero.egg.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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

}
