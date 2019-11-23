package com.zero.egg.service;

import com.github.pagehelper.PageInfo;
import com.zero.egg.requestDTO.AddCartGoodRequestDTO;
import com.zero.egg.requestDTO.DeleteCartGoodsRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderCartListRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsCheckRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsNumRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsSpecificationRequestDTO;
import com.zero.egg.tool.ServiceException;

/**
 * 购物车Service层
 */
public interface OrderCartService {

    /**
     * 加入购物车
     * @throws ServiceException
     * @param addCartGoodRequestDTO
     * @param loginUser
     */
    void addProductToCart(AddCartGoodRequestDTO addCartGoodRequestDTO, LoginUser loginUser) throws ServiceException;

    /**
     * 购物车列表
     * @param orderCartListRequestDTO
     * @return
     */
    PageInfo cartList(OrderCartListRequestDTO orderCartListRequestDTO) throws ServiceException;

    /**
     * 删除购物车商品
     * @param deleteCartGoodsRequestDTO
     * @throws ServiceException
     */
    void deleteCartGoods(DeleteCartGoodsRequestDTO deleteCartGoodsRequestDTO) throws ServiceException;

    /**
     * 更新购物车商品规格
     * @param updateCartGoodsSpecificationRequestDTO
     * @throws ServiceException
     */
    void updateCartGoodsSpecifacation(UpdateCartGoodsSpecificationRequestDTO updateCartGoodsSpecificationRequestDTO)throws ServiceException;

    /**
     * 更新购物车商品数量
     * @param updateCartGoodsNumRequestDTO
     */
    void updateCartGoodsNum(UpdateCartGoodsNumRequestDTO updateCartGoodsNumRequestDTO) throws ServiceException;

    /**
     * 更新购物车商品是否勾选
     * @param updateCartGoodsCheckRequestDTO
     */
    void updateCartGoodsCheck(UpdateCartGoodsCheckRequestDTO updateCartGoodsCheckRequestDTO) throws ServiceException;
}
