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
import org.springframework.transaction.annotation.Transactional;

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
             * 2.1 如果商品规格已经存在于购物车中,直接加数量
             * 3. 根据商品的规格id查询价格
             * 4. 将商品信息组装到购物车商品中
             */
            OrderCart orderCart = new OrderCart();
            OrderGoods orderGoods = orderGoodsMapper.selectOne(new QueryWrapper<OrderGoods>()
                    .eq("id", addCartGoodRequestDTO.getGoodsId())
                    .eq("shop_id", addCartGoodRequestDTO.getShopId())
                    .eq("is_on_sale", true)
                    .eq("dr", false));
            OrderGoodsSpecification orderGoodsSpecification = orderGoodsSpecificationMapper.selectOne(new QueryWrapper<OrderGoodsSpecification>()
                    .eq("id", addCartGoodRequestDTO.getGoodSpecificationId())
                    .eq("goods_id", orderGoods.getId())
                    .eq("dr", false));
            //如果相同规格的商品已经存在于购物车中,则直接改变数量
            Integer isExist = orderCartMapper.selectCount(new QueryWrapper<OrderCart>()
                    .eq("shop_id", addCartGoodRequestDTO.getShopId())
                    .eq("user_id", loginUser.getId())
                    .eq("goods_id", addCartGoodRequestDTO.getGoodsId())
                    .eq("good_specification_id", orderGoodsSpecification.getId())
                    .eq("weight_mode", addCartGoodRequestDTO.getWeightMode())
                    .eq("dr", false));
            if (isExist == 1) {
                Integer existCount = orderCartMapper.selectOne(new QueryWrapper<OrderCart>()
                        .select("number")
                        .eq("shop_id", addCartGoodRequestDTO.getShopId())
                        .eq("user_id", loginUser.getId())
                        .eq("goods_id", addCartGoodRequestDTO.getGoodsId())
                        .eq("good_specification_id", orderGoodsSpecification.getId())
                        .eq("weight_mode", addCartGoodRequestDTO.getWeightMode())
                        .eq("dr", false)).getNumber();
                Integer updateCount = existCount + addCartGoodRequestDTO.getNumber();
                orderCartMapper.update(orderCart.setNumber(updateCount), new UpdateWrapper<OrderCart>()
                        .eq("shop_id", addCartGoodRequestDTO.getShopId())
                        .eq("user_id", loginUser.getId())
                        .eq("goods_id", addCartGoodRequestDTO.getGoodsId())
                        .eq("good_specification_id", orderGoodsSpecification.getId())
                        .eq("weight_mode", addCartGoodRequestDTO.getWeightMode())
                        .eq("dr", false));
            } else if (isExist == 0) {
                orderCart = compactToOrderCartGood(orderGoods, addCartGoodRequestDTO, orderGoodsSpecification, loginUser);
                orderCartMapper.insert(orderCart);
            } else {
                throw new ServiceException("数据异常,请联系管理员");
            }
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
    @Transactional
    public void updateCartGoodsSpecifacation(UpdateCartGoodsSpecificationRequestDTO updateCartGoodsSpecificationRequestDTO) throws ServiceException {
        try {
            /**
             * TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端shopId
             * 2.查询购物车是否有更改后的规格商品,如果有,数量+1
             * 3.否则更新该用户在该店铺下的指定商品规格信息
             */
            OrderGoodsSpecification orderGoodsSpecification = orderGoodsSpecificationMapper.selectOne(new QueryWrapper<OrderGoodsSpecification>()
                    .eq("id", updateCartGoodsSpecificationRequestDTO.getGoodSpecificationId())
                    .eq("dr", false)
                    .eq("goods_id", updateCartGoodsSpecificationRequestDTO.getGoodsId()));
            if (null == orderGoodsSpecification) {
                throw new ServiceException("所选新规格为空");
            }
            //是否只需要改变数量
            boolean changeNumberFlag = false;
            //只需要改变数量的cartId
            String changeNumberCartId = null;
            //查询购物车里面的所有商品,循环判断
            List<OrderCart> cartList = orderCartMapper.getCartList(updateCartGoodsSpecificationRequestDTO.getShopId(), updateCartGoodsSpecificationRequestDTO.getUserId());
            for (OrderCart orderCart : cartList) {
                //如果要换的规格在与购物车中存在
                if (orderCart.getGoodsId().equals(orderGoodsSpecification.getGoodsId()) && orderCart.getGoodSpecificationId().equals(orderGoodsSpecification.getId())) {
                    changeNumberFlag = true;
                    changeNumberCartId = orderCart.getId();
                    break;
                }
            }
            if (changeNumberFlag) {
                Integer number = orderCartMapper.selectOne(new QueryWrapper<OrderCart>()
                        .select("number")
                        .eq("id", updateCartGoodsSpecificationRequestDTO.getCartId())
                        .eq("shop_id", updateCartGoodsSpecificationRequestDTO.getShopId())
                        .eq("user_id", updateCartGoodsSpecificationRequestDTO.getUserId())
                        .eq("dr", false)).getNumber();
                orderCartMapper.update(new OrderCart().setNumber(number), new UpdateWrapper<OrderCart>()
                        .eq("id", changeNumberCartId)
                        .eq("shop_id", updateCartGoodsSpecificationRequestDTO.getShopId())
                        .eq("user_id", updateCartGoodsSpecificationRequestDTO.getUserId()));
            } else {
                OrderCart orderCart = new OrderCart();
                orderCart.setWeightMode(updateCartGoodsSpecificationRequestDTO.getWeightMode());
                orderCart.setGoodSpecificationId(updateCartGoodsSpecificationRequestDTO.getGoodSpecificationId());
                orderCart.setGoodSpecificationName(orderGoodsSpecification.getSpecification());
                orderCart.setGoodSpecificationValue(orderGoodsSpecification.getValue());
                orderCart.setPrice(orderGoodsSpecification.getPrice());
                orderCart.setPicUrl(orderGoodsSpecification.getPicUrl());
                orderCartMapper.update(orderCart, new UpdateWrapper<OrderCart>()
                        .eq("id", updateCartGoodsSpecificationRequestDTO.getCartId())
                        .eq("shop_id", updateCartGoodsSpecificationRequestDTO.getShopId())
                        .eq("user_id", updateCartGoodsSpecificationRequestDTO.getUserId()));
            }
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
