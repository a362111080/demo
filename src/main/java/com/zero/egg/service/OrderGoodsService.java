package com.zero.egg.service;

import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

public interface OrderGoodsService {
    /**
     * 获取所选择门店的商品信息
     * @param model
     * @param loginUser
     * @return
     */
    Message getGoodsList(OrderGoodsRequestDTO model, LoginUser loginUser) throws ServiceException;

    /**
     * 根据门店和类目获取商品列表
     * @param model
     * @param loginUser
     * @return
     * @throws ServiceException
     */
    Message getGoodsListByCategoryId(OrderGoodsRequestDTO model, LoginUser loginUser) throws ServiceException;
}
