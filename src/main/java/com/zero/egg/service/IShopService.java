package com.zero.egg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.Shop;
import com.zero.egg.requestDTO.LoginUser;
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
public interface IShopService extends IService<Shop> {
	
	Message<Object> save(Shop entity ,LoginUser loginUser) ;

    List<Shop> getShopListByCompanid(String companyId);
}
