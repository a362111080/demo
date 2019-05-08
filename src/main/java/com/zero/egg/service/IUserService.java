package com.zero.egg.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.User;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.UserRequest;
import com.zero.egg.tool.Message;

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
	
	Message<Object> deleteBatchById(String ids,LoginUser loginUser);
	
	Message<Object> updateById(User entity,LoginUser loginUser);

}
