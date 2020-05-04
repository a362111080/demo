package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.dao.BdCityMapper;
import com.zero.egg.dao.OrderAddressMapper;
import com.zero.egg.dao.OrderBillDetailMapper;
import com.zero.egg.dao.OrderBillMapper;
import com.zero.egg.dao.OrderCartMapper;
import com.zero.egg.dao.OrderCategoryMapper;
import com.zero.egg.dao.OrderGoodsMapper;
import com.zero.egg.dao.OrderGoodsSpecificationMapper;
import com.zero.egg.dao.OrderSecretMapper;
import com.zero.egg.dao.OrderUserSecretMapper;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.dao.WechatAuthMapper;
import com.zero.egg.enums.BillEnums;
import com.zero.egg.model.BdCity;
import com.zero.egg.model.OrderAddress;
import com.zero.egg.model.OrderBill;
import com.zero.egg.model.OrderBillDetail;
import com.zero.egg.model.OrderCart;
import com.zero.egg.model.OrderCategory;
import com.zero.egg.model.OrderGoods;
import com.zero.egg.model.OrderGoodsSpecification;
import com.zero.egg.model.OrderSecret;
import com.zero.egg.model.OrderUserSecret;
import com.zero.egg.model.Shop;
import com.zero.egg.model.WechatAuth;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderDirectPurchaseRequestDTO;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.service.OrderGoodsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private OrderCategoryMapper orderCategoryMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private OrderCartMapper orderCartMapper;

    @Autowired
    private OrderGoodsSpecificationMapper orderGoodsSpecificationMapper;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private BdCityMapper bdCityMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private OrderBillMapper orderBillMapper;

    @Autowired
    private OrderBillDetailMapper orderBillDetailMapper;

    @Autowired
    private OrderUserSecretMapper orderUserSecretMapper;

    @Autowired
    private OrderSecretMapper orderSecretMapper;

    @Autowired
    private WechatAuthMapper wechatAuthMapper;


    @Override
    public Message getGoodsList(OrderGoodsRequestDTO model, LoginUser loginUser) throws ServiceException {
        Message message = new Message();
        try {
            /**
             * 1.判断传入的店铺id在不在用户绑定的店铺id之中
             * 2.查询上架且未删除的商品信息,可以根据商品名模糊查询
             */
            boolean flag = checkShopInUser(model.getShopId(), loginUser.getId());
            if (!flag) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_COOPERATE_SHOP);
                return message;
            }
            PageHelper.startPage(model.getCurrent().intValue(), model.getSize().intValue());
            List<OrderGoods> orderGoods = orderGoodsMapper.getAllByShopId(model);
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
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("getGoodsList service error");
        }
    }

    @Override
    public Message getGoodsListByCategoryId(OrderGoodsRequestDTO model, LoginUser loginUser) throws ServiceException {
        Message message = new Message();
        try {
            /**
             * 1.判断传入的店铺id在不在用户绑定的店铺id之中
             * 2.查询上架且未删除的商品信息,可以根据商品名模糊查询
             */
            boolean flag = checkShopInUser(model.getShopId(), loginUser.getId());
            if (!flag) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_COOPERATE_SHOP);
                return message;
            }
            PageHelper.startPage(model.getCurrent().intValue(), model.getSize().intValue());
            List<OrderGoods> orderGoods = orderGoodsMapper.getAllByShopIdAndCategoryId(model);
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
            if (e instanceof ServiceException) {
                throw e;
            }
            log.error("getGoodsList service error" + e);
            throw new ServiceException("getGoodsList service error");
        }
    }

    @Override
    @Transactional
    public Message directToPurchase(OrderDirectPurchaseRequestDTO orderDirectPurchaseRequestDTO) throws ServiceException {
        Message message = new Message();
        try {
            /**
             * 1.判断传入的店铺id在不在用户绑定的店铺id之中
             * 2.将商品信息加入购物车,获取购物车商品id
             * 3.确认订单
             */
            boolean flag = checkShopInUser(orderDirectPurchaseRequestDTO.getShopId(), orderDirectPurchaseRequestDTO.getUserId());
            if (!flag) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_COOPERATE_SHOP);
                return message;
            }
            OrderCart orderCart;
            OrderGoods orderGoods = orderGoodsMapper.selectOne(new QueryWrapper<OrderGoods>()
                    .eq("id", orderDirectPurchaseRequestDTO.getGoodsId())
                    .eq("shop_id", orderDirectPurchaseRequestDTO.getShopId())
                    .eq("is_on_sale", true)
                    .eq("dr", false));
            OrderGoodsSpecification orderGoodsSpecification = orderGoodsSpecificationMapper.selectOne(new QueryWrapper<OrderGoodsSpecification>()
                    .eq("id", orderDirectPurchaseRequestDTO.getGoodSpecificationId())
                    .eq("goods_id", orderGoods.getId())
                    .eq("dr", false));
            orderCart = compactToOrderCartGood(orderGoods, orderDirectPurchaseRequestDTO, orderGoodsSpecification);
            orderCartMapper.insert(orderCart);
            //购物车商品id
            String cartId = orderCart.getId();
            //确认订单
            String addressId = orderDirectPurchaseRequestDTO.getAddressId();
            OrderAddress orderAddress = orderAddressMapper.selectOne(new QueryWrapper<OrderAddress>()
                    .eq("id", addressId)
                    .eq("dr", false)
                    .eq("user_id", orderDirectPurchaseRequestDTO.getUserId()));
            if (null == orderAddress) {
                throw new ServiceException("选择的地址有误!");
            }
            OrderBill orderBill = new OrderBill();
            orderBill.setUserId(orderDirectPurchaseRequestDTO.getUserId());
            orderBill.setAddressId(addressId);
            orderBill.setConsignee(orderAddress.getName());
            orderBill.setMobile(orderAddress.getTel());
            //获取省市区地址
            String mergerName = bdCityMapper.selectOne(new QueryWrapper<BdCity>()
                    .select("merger_name")
                    .eq("id", orderAddress.getCityId()))
                    .getMergerName();
            //订单地址为省市区+详细地址
            orderBill.setAddress(mergerName + orderAddress.getAddressDetail());
            orderBill.setCreator(orderDirectPurchaseRequestDTO.getUserId());
            orderBill.setCreatetime(new Date());
            Shop shop = shopMapper.selectOne(new QueryWrapper<Shop>()
                    .select("company_id", "name")
                    .eq("id", orderDirectPurchaseRequestDTO.getShopId())
                    .eq("dr", false));
            String companyId = shop.getCompanyId();
            orderBill.setShopName(shop.getName());
            orderBill.setCompanyId(companyId);
            orderBill.setShopId(orderDirectPurchaseRequestDTO.getShopId());
            if (StringUtils.isNotBlank(orderDirectPurchaseRequestDTO.getMessage())) {
                orderBill.setMessage(orderDirectPurchaseRequestDTO.getMessage());
            }
            OrderBillDetail orderBillDetail = new OrderBillDetail();
            BigDecimal total = BigDecimal.ZERO;
            orderBillDetail.setQuantity(BigDecimal.valueOf(orderCart.getNumber()));
            if (checkBigDecimal(orderCart.getPrice())) {
                orderBillDetail.setGoodsPrice(new BigDecimal(orderCart.getPrice()));
                orderBillDetail.setSubtotal(orderBillDetail.getGoodsPrice().multiply(orderBillDetail.getQuantity()));
                total = total.add(orderBillDetail.getSubtotal());
                orderBill.setTotalPrice(total);
            }
            orderBillDetail.setCompanyId(companyId);
            orderBillDetail.setShopId(orderDirectPurchaseRequestDTO.getShopId());
            orderBillDetail.setCartId(cartId);
            String categoryId = orderGoodsMapper.selectOne(new QueryWrapper<OrderGoods>()
                    .select("category_id")
                    .eq("id", orderCart.getGoodsId())
                    .eq("company_id", companyId)
                    .eq("shop_id", orderDirectPurchaseRequestDTO.getShopId())
                    .eq("dr", false))
                    .getCategoryId();
            String categoryName = orderCategoryMapper.selectOne(new QueryWrapper<OrderCategory>()
                    .select("name")
                    .eq("id", categoryId)
                    .eq("company_id", companyId)
                    .eq("shop_id", orderDirectPurchaseRequestDTO.getShopId())
                    .eq("dr", false))
                    .getName();
            orderBillDetail.setOrderCategoryId(categoryId);
            orderBillDetail.setOrderCategoryName(categoryName);
            orderBillDetail.setSpecificationId(orderCart.getGoodSpecificationId());
            orderBillDetail.setSpecificationName(orderCart.getGoodSpecificationName());
            orderBillDetail.setSpecificationValue(orderCart.getGoodSpecificationValue());
            orderBillDetail.setCreatetime(new Date());
            //设置SN
            Integer count = orderBillMapper.selectCount(null);
            DecimalFormat g1 = new DecimalFormat("000000000");
            orderBill.setOrderSn(g1.format(count + 1));
            orderBill.setOrderStatus(BillEnums.OrderStatus.Missed.index());
            orderBillMapper.insert(orderBill);
            orderBillDetail.setOrderId(orderBill.getId());
            orderBillDetailMapper.insert(orderBillDetail);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("directToPurchase service error" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("directToPurchase service error");
        }
    }

    /**
     * 将商品信息封装到购物车商品信息
     *
     * @param orderGoods              商品基本信息
     * @param orderGoodsSpecification 商品规格信息
     */
    private OrderCart compactToOrderCartGood(OrderGoods orderGoods, OrderDirectPurchaseRequestDTO orderDirectPurchaseRequestDTO,
                                             OrderGoodsSpecification orderGoodsSpecification) {
        OrderCart orderCart = new OrderCart();
        orderCart.setShopId(orderGoods.getShopId());
        orderCart.setUserId(orderDirectPurchaseRequestDTO.getUserId());
        orderCart.setGoodSpecificationId(orderGoodsSpecification.getId());
        orderCart.setGoodSpecificationName(orderGoodsSpecification.getSpecification());
        orderCart.setGoodSpecificationValue(orderGoodsSpecification.getValue());
        orderCart.setGoodsId(orderGoods.getId());
        orderCart.setGoodsSn(orderGoods.getGoodsSn());
        orderCart.setGoodsName(orderGoods.getName());
        orderCart.setWeightMode(orderDirectPurchaseRequestDTO.getWeightMode());
        orderCart.setPrice(orderGoodsSpecification.getPrice());
        orderCart.setNumber(orderDirectPurchaseRequestDTO.getNumber());
        orderCart.setChecked(true);
        orderCart.setPicUrl(orderGoodsSpecification.getPicUrl());
        orderCart.setCreatetime(new Date());
        //直接购买可以直接让购物车商品作废,只是为了获取购物车商品id
        orderCart.setDr(true);
        return orderCart;
    }

    /**
     * 判断String能否转换成BigDecimal
     *
     * @param targetString
     * @return
     */
    private boolean checkBigDecimal(String targetString) {
        BigDecimal tmp = null;
        try {
            if (null == targetString) {
                return false;
            }
            tmp = new BigDecimal(targetString);
        } catch (NumberFormatException e) {

        }
        if (tmp != null) {
            return true;
        }
        return false;
    }

    /**
     * 判断传入的店铺id在不在用户绑定的店铺id之中
     * @param shopId
     * @param userId
     * @return
     */
    private boolean checkShopInUser(String shopId,String userId) {
        try {
            WechatAuth wechatAuth = wechatAuthMapper.selectById(userId);
            List<OrderUserSecret> orderUserSecrets = orderUserSecretMapper.selectList(new QueryWrapper<OrderUserSecret>()
                    .eq("user_id", wechatAuth.getWechatAuthId())
                    .eq("dr", 0));
            //如果没有有效的绑定秘钥信息,则返回空
            if (orderUserSecrets.size() < 1 || null == orderUserSecrets) {
                throw new ServiceException(UtilConstants.ResponseMsg.NO_COOPERATE_SHOP);
            }
            List<String> shops = new ArrayList<>();
            Shop shop;
            OrderSecret orderSecret;
            for (OrderUserSecret orderUserSecret : orderUserSecrets) {
                orderSecret = orderSecretMapper.selectOne(new QueryWrapper<OrderSecret>()
                        .select("shop_id", "company_id", "secret_key")
                        .eq("id", orderUserSecret.getSecretId())
                        .eq("dr", 0)
                        .eq("status", 1));
                if (null == orderSecret) {
                    continue;
                }
                shop = shopMapper.selectOne(new QueryWrapper<Shop>()
                        .eq("id", orderSecret.getShopid())
                        .eq("company_id", orderSecret.getCompanyid())
                        .eq("dr", 0));
                shops.add(shop.getId());
            }
            if (shops.contains(shopId)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("checkShopInUser error:" + e);
            if (e instanceof ServiceException) {
                throw e;
            } else {
                throw new ServiceException("checkShopInUser error:" + e);
            }
        }
    }
}
