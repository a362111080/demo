package com.zero.egg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.UserMapper;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.enums.UserEnums;
import com.zero.egg.model.Shop;
import com.zero.egg.model.User;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.UserRequest;
import com.zero.egg.service.IShopService;
import com.zero.egg.service.IUserService;
import com.zero.egg.tool.MD5Utils;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.StringTool;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;

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

	@Autowired
	private IShopService shopService;
	
	
	
	@Override
	public Message<IPage<User>> listPage(UserRequest user, LoginUser loginUser) {
		Message<IPage<User>> message = new Message<IPage<User>>();
		if (StringUtils.isNotBlank(loginUser.getCompanyId())) {
			user.setCompanyId(loginUser.getCompanyId());
		}
		if (StringUtils.isNotBlank(loginUser.getShopId())) {
			user.setShopId(loginUser.getShopId());
		}
		Page<User> page = new Page<>();
		page.setCurrent(user.getCurrent());
		page.setSize(user.getSize());
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (user != null) {
			queryWrapper.like(StringUtils.isNotBlank(user.getName()),"name", user.getName())
			.like(StringUtils.isNotBlank(user.getCode()),"code", user.getCode())
			.eq(StringUtils.isNotBlank(user.getStatus()), "status", user.getStatus())
			.eq(StringUtils.isNotBlank(user.getCompanyId()),"company_id",user.getCompanyId())
			.eq(StringUtils.isNotBlank(user.getShopId()), "shop_id", user.getShopId());
		}
		IPage<User> list = page(page, queryWrapper);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		message.setData(list);
		return message;
	}

	@Override
	public Message<List<User>> listAll(UserRequest user, LoginUser loginUser) {
		Message<List<User>> message = new Message<List<User>>();
		if (StringUtils.isNotBlank(loginUser.getCompanyId())) {
			user.setCompanyId(loginUser.getCompanyId());
		}
		if (StringUtils.isNotBlank(loginUser.getShopId())) {
			user.setShopId(loginUser.getShopId());
		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (user != null) {
			queryWrapper.like(StringUtils.isNotBlank(user.getName()),"name", user.getName())
			.like(StringUtils.isNotBlank(user.getCode()),"code", user.getCode())
			.eq(StringUtils.isNotBlank(user.getStatus()), "status", user.getStatus())
			.eq(StringUtils.isNotBlank(user.getCompanyId()),"company_id",user.getCompanyId())
			.eq(StringUtils.isNotBlank(user.getType().toString()),"type",user.getType())
			.eq(StringUtils.isNotBlank(user.getShopId()), "shop_id", user.getShopId());
		}
		List<User> userList = list(queryWrapper);
		if (userList != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(userList);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}

	
	@Override
	public Message<Object> save(User user, LoginUser loginUser) {
		Message<Object> message = new Message<Object>();
		if (StringUtils.isNotBlank(loginUser.getCompanyId())) {
			user.setCompanyId(loginUser.getCompanyId());
		}
		if (StringUtils.isNotBlank(loginUser.getShopId())) {
			user.setShopId(loginUser.getShopId());
		}
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
		
		if (StringUtils.isBlank(user.getPassword())) {
			user.setPassword("888888");
		}
		user.setModifier(loginUser.getId());
		user.setCreator(loginUser.getId());
		String shopId = user.getShopId();
		Shop shop = shopService.getById(shopId);
		if (shop != null) {
			Integer count;
			boolean checkResult = true;
			QueryWrapper<User> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("shop_id", user.getShopId())
			.eq("dr", false);
			//检查人员类型数量是否有空余
			if (UserEnums.Type.Pc.index().equals(user.getType())) {
				count = shop.getPcClient();
				queryWrapper.eq("type", UserEnums.Type.Pc.index());
				List<User> userList = list(queryWrapper);
				if (userList != null && userList.size() >= count) {
					message.setMessage("Pc客户端名额已用完");
					checkResult = false;
				}
			}else if (UserEnums.Type.Boss.index().equals(user.getType())) {
				count = shop.getBossClient();
				queryWrapper.eq("type", UserEnums.Type.Boss.index());
				List<User> userList = list(queryWrapper);
				if (userList != null && userList.size() >= count) {
					message.setMessage("Boss客户端名额已用完");
					checkResult = false;
				}
			}else if (UserEnums.Type.Staff.index().equals(user.getType())) {
				count = shop.getStaffClient();
				queryWrapper.eq("type", UserEnums.Type.Staff.index());
				List<User> userList = list(queryWrapper);
				if (userList != null && userList.size() >= count) {
					message.setMessage("员工客户端名额已用完");
					checkResult = false;
				}
			}else if (UserEnums.Type.Device.index().equals(user.getType())) {
				count = shop.getDeviceClient();
				queryWrapper.eq("type", UserEnums.Type.Device.index());
				List<User> userList = list(queryWrapper);
				if (userList != null && userList.size() >= count) {
					message.setMessage("设备客户端名额已用完");
					checkResult = false;
				}
			}
			if (checkResult) {
				if (save(user)) {
					message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
					message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
				}else {
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage(UtilConstants.ResponseMsg.FAILED);
				}
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			}
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("店铺不存在！");
		}
		return message;
	}
	
	
	@Override
	@Transactional(rollbackOn=Exception.class)
	public Message<Object> deleteById(String id, LoginUser loginUser) {
		Message<Object> message = new Message<Object>();
		User oldUser = getById(id);
		if (oldUser != null) {
			if (UserEnums.Status.Disable.index().equals(oldUser.getStatus())) {
				Shop shop = shopService.getById(oldUser.getShopId());
				if (shop != null) {
					User user = new User();
					user.setDr(true);
					user.setId(id);
					user.setModifier(loginUser.getId());
					user.setModifytime(new Date());
					if (updateById(user)) {//逻辑删除
						message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
						message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
						
					}else {
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage(UtilConstants.ResponseMsg.FAILED);
					}
				}else {
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("员工所在店铺不存在");
				}
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage("员工未离职，不能删除");
			}
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("员工信息缺失");
		}
		return message;
	}
	
	
	
	@Override
	@Transactional(rollbackOn=Exception.class)
	public Message<Object> deleteBatchById(String ids,LoginUser loginUser) {
		Message<Object> message = new Message<>();
		List<String> idsList = StringTool.splitToList(ids, ",");
		if (idsList !=null) {
			List<User> userList = new ArrayList<>();
			for (String id : idsList) {
				User oldUser = getById(ids);
				if (UserEnums.Status.Normal.index().equals(oldUser.getStatus())) {
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("有员工为离职，删除失败");
					return message;
				}
				//添加删除用户对象
				User user = new User();
				user.setDr(true);
				user.setId(id);
				user.setModifier(loginUser.getId());
				user.setModifytime(new Date());
				userList.add(user);
			}
			if (updateBatchById(userList)) {//逻辑删除
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("数据缺失，请检查数据");
		}
		return message;
	}

	@Override
	public Message<Object> updateById(User user, LoginUser loginUser) {
		Message<Object> message = new Message<>();
		user.setModifier(loginUser.getId());
		user.setModifytime(new Date());
		user.setStatus(UserEnums.Status.Disable.index().toString());
		if (updateById(user)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}

	
	
}
