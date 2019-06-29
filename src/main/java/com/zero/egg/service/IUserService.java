package com.zero.egg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.User;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.UserRequest;
import com.zero.egg.tool.Message;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
public interface IUserService extends IService<User> {
	
	
	Message<IPage<User>> listPage(UserRequest user,LoginUser loginUser);
	
	
	Message<List<User>> listAll(UserRequest user,LoginUser loginUser);
	
	
	Message<Object> save(User entity ,LoginUser loginUser) ;
	
	Message<Object> deleteById(String ids,LoginUser loginUser);
	
	Message<Object> deleteBatchById(List<User> userList,LoginUser loginUser);
	
	Message<Object> updateById(User entity,LoginUser loginUser);

	/**
	 * 停用用户
	 * @param entity
	 * @param loginUser
	 * @return
	 */
	Message<Object> dismissById(User entity,LoginUser loginUser);

	User getUserinfo(User user);
}
