package com.zero.egg.service.impl;

import com.zero.egg.model.User;
import com.zero.egg.dao.UserMapper;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.service.IUserService;
import com.zero.egg.tool.MD5Utils;
import com.zero.egg.tool.UuidUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	@Override
	public boolean save(User user) {
		user.setId(UuidUtil.get32UUID());
		user.setCreatetime(new Date());
		user.setModifytime(new Date());
		user.setStatus(CompanyUserEnums.Status.Normal.index().toString());
		user.setDr(false);
		if (StringUtils.isNotBlank(user.getPassword())) {
			try {
				user.setPassword(MD5Utils.encode(user.getPassword()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.save(user);
		
	}
}
