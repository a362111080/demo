package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.dao.OrderCartMapper;
import com.zero.egg.dao.OrderGoodsMapper;
import com.zero.egg.dao.OrderGoodsSpecificationMapper;
import com.zero.egg.model.OrderCart;
import com.zero.egg.model.OrderGoods;
import com.zero.egg.model.OrderGoodsSpecification;
import com.zero.egg.requestDTO.AddCartGoodRequestDTO;
import com.zero.egg.requestDTO.DeleteCartGoodsRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderCartListRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsCheckRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsNumRequestDTO;
import com.zero.egg.requestDTO.UpdateCartGoodsSpecificationRequestDTO;
import com.zero.egg.service.OrderCartService;
import com.zero.egg.tool.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderCartServiceImpl
 * @Author lyming
 * @Date 2019/9/30 3:26 下午
 **/
@Slf4j
@Service
public class OrderCartServiceImpl implements OrderCartService {

    @Autowired
    private OrderCartMapper orderCartMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderGoodsSpecificationMapper orderGoodsSpecificationMapper;

    @Override
    public void addProductToCart(AddCartGoodRequestDTO addCartGoodRequestDTO, LoginUser loginUser) throws ServiceException {
        try {
            /**
             *  TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端addCartGoodRequestDTO中的shopId
             * 2. 根据shopId和goods_id查询商品信息
             * 3. 根据商品的规格id查询价格
             * 3. 将商品信息组装到购物车商品中
             */
            OrderCart orderCart = new OrderCart();
            OrderGoods orderGoods = orderGoodsMapper.selectOne(new QueryWrapper<OrderGoods>()
                    .eq("id", addCartGoodRequestDTO.getGoods_id())
                    .eq("shop_id", addCartGoodRequestDTO.getShop_id())
                    .eq("is_on_sale", true)
                    .eq("dr", false));
            OrderGoodsSpecification orderGoodsSpecification = orderGoodsSpecificationMapper.selectOne(new QueryWrapper<OrderGoodsSpecification>()
                    .eq("id",addCartGoodRequestDTO.getGoodSpecificationId())
                    .eq("goods_id", orderGoods.getId())
                    .eq("dr", false));
            orderCart = compactToOrderCartGood(orderGoods, addCartGoodRequestDTO, orderGoodsSpecification, loginUser);
            orderCartMapper.insert(orderCart);
        } catch (Exception e) {
            log.error("addProductToCart failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("addProductToCart failed" + e);
        }
    }

    @Override
    public PageInfo cartList(OrderCartListRequestDTO orderCartListRequestDTO) {
        try {
            /**
             * TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端shopId
             * 2.查询该用户在该店铺下的所有购物车信息(包括规格列表)
             */
            PageHelper.startPage(orderCartListRequestDTO.getCurrent().intValue(), orderCartListRequestDTO.getSize().intValue());
            List<OrderCart> cartList = orderCartMapper.getCartList(orderCartListRequestDTO.getShopId(), orderCartListRequestDTO.getUserId());
            PageInfo pageInfo = new PageInfo(cartList);
            return pageInfo;
        } catch (Exception e) {
            log.error("cartList failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("cartList failed" + e);
        }
    }

    @Override
    public void deleteCartGoods(DeleteCartGoodsRequestDTO deleteCartGoodsRequestDTO) throws ServiceException {
        try {
            /**
             * TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端shopId
             * 2.删除该用户在该店铺下的指定商品
             */
            orderCartMapper.update(new OrderCart().setDr(true), new UpdateWrapper<OrderCart>()
                    .in("id", deleteCartGoodsRequestDTO.getIds())
                    .eq("shop_id", deleteCartGoodsRequestDTO.getShopId())
                    .eq("user_id", deleteCartGoodsRequestDTO.getUserId()));
        } catch (Exception e) {
            log.error("cartList failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("cartList failed" + e);
        }
    }

    @Override
    public void updateCartGoodsSpecifacation(UpdateCartGoodsSpecificationRequestDTO updateCartGoodsSpecificationRequestDTO) throws ServiceException {
        try {
            /**
             * TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端shopId
             * 2.更新该用户在该店铺下的指定商品规格信息
             */
            OrderGoodsSpecification orderGoodsSpecification = orderGoodsSpecificationMapper.selectOne(new QueryWrapper<OrderGoodsSpecification>()
                    .eq("id", updateCartGoodsSpecificationRequestDTO.getGoodSpecificationId())
                    .eq("dr", false)
                    .eq("goods_id", updateCartGoodsSpecificationRequestDTO.getGoodsId()));
            if (null == orderGoodsSpecification) {
                throw new ServiceException("所选新规格为空");
            }
            OrderCart orderCart = new OrderCart();
            orderCart.setWeightMode(updateCartGoodsSpecificationRequestDTO.getWeightMode());
            orderCart.setGoodSpecificationId(updateCartGoodsSpecificationRequestDTO.getGoodSpecificationId());
            orderCart.setGoodSpecificationName(orderGoodsSpecification.getSpecification());
            orderCart.setGoodSpecificationValue(orderGoodsSpecification.getValue());
            orderCart.setPrice(orderGoodsSpecification.getPrice());
            orderCartMapper.update(orderCart, new UpdateWrapper<OrderCart>()
                    .eq("id", updateCartGoodsSpecificationRequestDTO.getCartId())
                    .eq("shop_id", updateCartGoodsSpecificationRequestDTO.getShopId())
                    .eq("user_id", updateCartGoodsSpecificationRequestDTO.getUserId()));
        } catch (Exception e) {
            log.error("updateCartGoodsSpecifacation failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("updateCartGoodsSpecifacation failed" + e);
        }
    }

    @Override
    public void updateCartGoodsNum(UpdateCartGoodsNumRequestDTO updateCartGoodsNumRequestDTO) throws ServiceException {
        try {
            /**
             * TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端shopId
             * 2.更新该用户在该店铺下的指定商品数量
             */
            OrderCart orderCart = new OrderCart();
            orderCart.setNumber(updateCartGoodsNumRequestDTO.getNumber());
            orderCartMapper.update(orderCart, new UpdateWrapper<OrderCart>()
                    .eq("id", updateCartGoodsNumRequestDTO.getCartId())
                    .eq("shop_id", updateCartGoodsNumRequestDTO.getShopId())
                    .eq("user_id", updateCartGoodsNumRequestDTO.getUserId()));
        } catch (Exception e) {
            log.error("updateCartGoodsNum failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("updateCartGoodsNum failed" + e);
        }
    }

    @Override
    public void updateCartGoodsCheck(UpdateCartGoodsCheckRequestDTO updateCartGoodsCheckRequestDTO) throws ServiceException {
        try {
            /**
             * TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端shopId
             * 2.更新该用户在该店铺下的指定商品数量
             */
            OrderCart orderCart = new OrderCart();
            orderCart.setChecked(updateCartGoodsCheckRequestDTO.getChecked());
            orderCartMapper.update(orderCart, new UpdateWrapper<OrderCart>()
                    .eq("id", updateCartGoodsCheckRequestDTO.getCartId())
                    .eq("shop_id", updateCartGoodsCheckRequestDTO.getShopId())
                    .eq("user_id", updateCartGoodsCheckRequestDTO.getUserId()));
        } catch (Exception e) {
            log.error("updateCartGoodsCheck failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("updateCartGoodsCheck failed" + e);
        }
    }

    /**
     * 将商品信息封装到购物车商品信息
     *
     * @param orderGoods              商品基本信息
     * @param orderGoodsSpecification 商品规格信息
     * @param loginUser               登录者信息
     */
    private OrderCart compactToOrderCartGood(OrderGoods orderGoods, AddCartGoodRequestDTO addCartGoodRequestDTO,
                                             OrderGoodsSpecification orderGoodsSpecification, LoginUser loginUser) {
        OrderCart orderCart = new OrderCart();
        orderCart.setShopId(orderGoods.getShopId());
        orderCart.setUserId(loginUser.getId());
        orderCart.setGoodSpecificationId(orderGoodsSpecification.getId());
        orderCart.setGoodSpecificationName(orderGoodsSpecification.getSpecification());
        orderCart.setGoodSpecificationValue(orderGoodsSpecification.getValue());
        orderCart.setGoodsId(orderGoods.getId());
        orderCart.setGoodsSn(orderGoods.getGoodsSn());
        orderCart.setGoodsName(orderGoods.getName());
        orderCart.setWeightMode(addCartGoodRequestDTO.getWeightMode());
        orderCart.setPrice(orderGoodsSpecification.getPrice());
        orderCart.setNumber(addCartGoodRequestDTO.getNumber());
        orderCart.setChecked(true);
        orderCart.setPicUrl(orderGoodsSpecification.getPicUrl());
        orderCart.setCreatetime(new Date());
        orderCart.setDr(false);
        return orderCart;
    }

}
