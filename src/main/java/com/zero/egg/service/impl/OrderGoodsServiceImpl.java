package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.dao.OrderGoodsMapper;
import com.zero.egg.model.OrderGoods;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.service.OrderGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName OrderGoodsServiceImpl
 * @Author lyming
 * @Date 2019/9/29 10:17 上午
 **/
@Service
@Slf4j
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Override
    public Message getGoodsList(OrderGoodsRequestDTO model, LoginUser loginUser) throws ServiceException {
        Message message = new Message();
        try {
            /**
             * TODO 1.判断传入的店铺id在不在用户绑定的店铺id之中
             * 2.查询上架且未删除的商品信息,可以根据商品名模糊查询
             */
            PageHelper.startPage(model.getCurrent().intValue(), model.getSize().intValue());
            List<OrderGoods> orderGoods = orderGoodsMapper.selectList(new QueryWrapper<OrderGoods>()
                    .eq("shop_id", model.getShopId())
                    .eq("dr", false)
                    .eq("is_on_sale", true)
                    .like(StringUtils.isNotBlank(model.getName()), "name", model.getName()));
            if (orderGoods.size() < 1) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage("店铺无商品信息");
                return message;
            }
            PageInfo<OrderGoods> pageInfo = new PageInfo<>(orderGoods);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setData(pageInfo);
            return message;
        } catch (Exception e) {
            log.error("getGoodsList service error" + e);
            throw new ServiceException("getGoodsList service error");
        }
    }

    @Override
    public Message getGoodsListByCategoryId(OrderGoodsRequestDTO model, LoginUser loginUser) throws ServiceException {
        Message message = new Message();
        try {
            /**
             * TODO 1.判断传入的店铺id在不在用户绑定的店铺id之中
             * 2.查询上架且未删除的商品信息,可以根据商品名模糊查询
             */
            PageHelper.startPage(model.getCurrent().intValue(), model.getSize().intValue());
            List<OrderGoods> orderGoods = orderGoodsMapper.selectList(new QueryWrapper<OrderGoods>()
                    .eq("shop_id", model.getShopId())
                    .eq("dr", false)
                    .eq("is_on_sale", true)
                    .eq("category_id",model.getCategoryId())
                    .like(StringUtils.isNotBlank(model.getName()), "name", model.getName()));
            if (orderGoods.size() < 1) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage("店铺无商品信息");
                return message;
            }
            PageInfo<OrderGoods> pageInfo = new PageInfo<>(orderGoods);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setData(pageInfo);
            return message;
        } catch (Exception e) {
            log.error("getGoodsList service error" + e);
            throw new ServiceException("getGoodsList service error");
        }
    }
}
