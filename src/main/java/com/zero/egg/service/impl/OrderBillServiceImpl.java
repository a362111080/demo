package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.dao.BdCityMapper;
import com.zero.egg.dao.OrderAddressMapper;
import com.zero.egg.dao.OrderBillDetailMapper;
import com.zero.egg.dao.OrderBillMapper;
import com.zero.egg.dao.OrderCartMapper;
import com.zero.egg.dao.OrderCategoryMapper;
import com.zero.egg.dao.OrderGoodsMapper;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.enums.BillEnums;
import com.zero.egg.model.BdCity;
import com.zero.egg.model.OrderAddress;
import com.zero.egg.model.OrderBill;
import com.zero.egg.model.OrderBillDetail;
import com.zero.egg.model.OrderCart;
import com.zero.egg.model.OrderCategory;
import com.zero.egg.model.OrderGoods;
import com.zero.egg.model.Shop;
import com.zero.egg.requestDTO.AddOrderBillRequestDTO;
import com.zero.egg.requestDTO.CancelMissedBillReqeustDTO;
import com.zero.egg.requestDTO.DeleteCompletedBillReqeustDTO;
import com.zero.egg.requestDTO.OrderBillListReqeustDTO;
import com.zero.egg.responseDTO.OrderBillListResponseDTO;
import com.zero.egg.service.OrderBillService;
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
 * @author lym
 */
@Slf4j
@Service
public class OrderBillServiceImpl implements OrderBillService {

    @Autowired
    private OrderBillMapper orderBillMapper;

    @Autowired
    private OrderBillDetailMapper orderBillDetailMapper;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private BdCityMapper bdCityMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private OrderCartMapper orderCartMapper;

    @Autowired
    private OrderCategoryMapper orderCategoryMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Override
    @Transactional
    public Message addNewBill(AddOrderBillRequestDTO addOrderBillRequestDTO) throws ServiceException {
        try {
            Message message = new Message();
            /**
             * TODO 验证登录用户绑定的shopIdList中是否有传过来的shopId
             * 1.从addOrderBillRequestDTO中获取addressId,,进而获取地址信息,检验地址有效性(是否未删除,是否是该登录用户的地址),并封装到OrderBill对象中
             * 2.从addOrderBillRequestDTO中获取cartId列表,进而获取购物车商品列表信息,检验有效性(是否未删除,是否是该登录用户的购物车商品)
             * 3.从购物车商品信息中获取商品id,进而获取商品类别信息,同时将规格信息封装到OrderBillDetail对象中
             * 4.遍历购物车商品列表,计算小计金额,如果商品单价有一个为null,则OrderBill对象中的总计为null,否则叠加小计金额到总计
             * 5.删除购物车商品信息
             */
            String addressId = addOrderBillRequestDTO.getAddressId();
            OrderAddress orderAddress = orderAddressMapper.selectOne(new QueryWrapper<OrderAddress>()
                    .eq("id", addressId)
                    .eq("dr", false)
                    .eq("user_id", addOrderBillRequestDTO.getUserId()));
            if (null == orderAddress) {
                throw new ServiceException("选择的地址有误!");
            }
            OrderBill orderBill = new OrderBill();
            orderBill.setUserId(addOrderBillRequestDTO.getUserId());
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
            orderBill.setCreator(addOrderBillRequestDTO.getUserId());
            orderBill.setCreatetime(new Date());
            Shop shop = shopMapper.selectOne(new QueryWrapper<Shop>()
                    .select("company_id", "name")
                    .eq("id", addOrderBillRequestDTO.getShopId())
                    .eq("dr", false));
            String companyId = shop.getCompanyId();
            orderBill.setShopName(shop.getName());
            orderBill.setCompanyId(companyId);
            orderBill.setShopId(addOrderBillRequestDTO.getShopId());
            if (StringUtils.isNotBlank(addOrderBillRequestDTO.getMessage())) {
                orderBill.setMessage(addOrderBillRequestDTO.getMessage());
            }
            //返回消息(可能有的商品有异常)
            StringBuffer sb = new StringBuffer();
            //总价是否需要订单细节小计累加标识
            Boolean totalPriceFlag = true;
            OrderBillDetail orderBillDetail;
            List<OrderBillDetail> orderBillDetailList = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            for (String cartId : addOrderBillRequestDTO.getCartIds()) {
                OrderCart orderCart = orderCartMapper.selectOne(new QueryWrapper<OrderCart>()
                        .eq("id", cartId)
                        .eq("shop_id", addOrderBillRequestDTO.getShopId())
                        .eq("user_id", addOrderBillRequestDTO.getUserId())
                        .eq("dr", false));
                //删除购物车商品信息
                orderCartMapper.update(new OrderCart().setDr(true), new UpdateWrapper<OrderCart>()
                        .eq("id", cartId)
                        .eq("shop_id", addOrderBillRequestDTO.getShopId())
                        .eq("user_id", addOrderBillRequestDTO.getUserId())
                        .eq("dr", false));
                if (null == orderCart) {
                    sb.append("购物车存在异常商品,商品名为" + orderCart.getGoodsName());
                    continue;
                }
                orderBillDetail = new OrderBillDetail();
                orderBillDetail.setQuantity(BigDecimal.valueOf(orderCart.getNumber()));
                if (totalPriceFlag) {
                    //如果单价不能转换成BigDecimal,则小计总价都为null
                    if (!checkBigDecimal(orderCart.getPrice())) {
                        totalPriceFlag = false;
                    } else {
                        orderBillDetail.setGoodsPrice(new BigDecimal(orderCart.getPrice()));
                        orderBillDetail.setSubtotal(orderBillDetail.getGoodsPrice().multiply(orderBillDetail.getQuantity()));
                        total = total.add(orderBillDetail.getSubtotal());
                    }
                }
                orderBillDetail.setCompanyId(companyId);
                orderBillDetail.setShopId(addOrderBillRequestDTO.getShopId());
                orderBillDetail.setCartId(cartId);
                String categoryId = orderGoodsMapper.selectOne(new QueryWrapper<OrderGoods>()
                        .select("category_id")
                        .eq("id", orderCart.getGoodsId())
                        .eq("company_id", companyId)
                        .eq("shop_id", addOrderBillRequestDTO.getShopId())
                        .eq("dr", false))
                        .getCategoryId();
                String categoryName = orderCategoryMapper.selectOne(new QueryWrapper<OrderCategory>()
                        .select("name")
                        .eq("id", categoryId)
                        .eq("company_id", companyId)
                        .eq("shop_id", addOrderBillRequestDTO.getShopId())
                        .eq("dr", false))
                        .getName();
                orderBillDetail.setOrderCategoryId(categoryId);
                orderBillDetail.setOrderCategoryName(categoryName);
                orderBillDetail.setSpecificationId(orderCart.getGoodSpecificationId());
                orderBillDetail.setSpecificationName(orderCart.getGoodSpecificationName());
                orderBillDetail.setSpecificationValue(orderCart.getGoodSpecificationValue());
                orderBillDetail.setCreatetime(new Date());
                orderBillDetailList.add(orderBillDetail);
            }
            //如果标识为true,则总账单有合计金额
            if (totalPriceFlag) {
                orderBill.setTotalPrice(total);
            }
            //设置SN
            Integer count = orderBillMapper.selectCount(null);
            DecimalFormat g1 = new DecimalFormat("000000000");
            orderBill.setOrderSn(g1.format(count + 1));
            orderBill.setOrderStatus(BillEnums.OrderStatus.Missed.index());
            orderBillMapper.insert(orderBill);
            for (OrderBillDetail orderBillDetail1 : orderBillDetailList) {
                orderBillDetail1.setOrderId(orderBill.getId());
                orderBillDetailMapper.insert(orderBillDetail1);
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            if (sb.length() > 0) {
                message.setMessage(sb.toString());
            } else {
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("addNewBill failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("addNewBill failed" + e);
        }

    }

    @Override
    public IPage listOrderBill(OrderBillListReqeustDTO orderBillListReqeustDTO) throws ServiceException {
        try {
            Page<OrderBill> page = new Page<OrderBill>(orderBillListReqeustDTO.getCurrent(), orderBillListReqeustDTO.getSize());
            QueryWrapper<OrderBill> queryWrapper = new QueryWrapper();
            queryWrapper.eq("oo.user_id", orderBillListReqeustDTO.getUserId())
                    .eq("oo.dr", false)
                    .eq(null != orderBillListReqeustDTO.getStatus(), "oo.order_status", orderBillListReqeustDTO.getStatus());
            IPage<OrderBillListResponseDTO> orderBillList = orderBillMapper.selectBillList(page,queryWrapper);
            for (OrderBillListResponseDTO orderBillListResponseDTO : orderBillList.getRecords()) {
                List<String> pics = orderBillMapper.selectBillListPics(orderBillListReqeustDTO.getUserId(), orderBillListResponseDTO.getId());
                orderBillListResponseDTO.setPics(pics);
            }
            return orderBillList;
        } catch (Exception e) {
            log.error("listOrderBill failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("listOrderBill failed" + e);
        }
    }

    @Override
    public void cancelorderBill(CancelMissedBillReqeustDTO cancelMissedBillReqeustDTO) throws ServiceException {
        try {
            /**
             * TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端shopId
             * 2.查询该订单是否为未接单的状态
             * 3.删除该用户在该店铺下的指定订单
             */
            Integer orderStatus = orderBillMapper.selectOne(new QueryWrapper<OrderBill>()
                    .select("order_status")
                    .eq("id", cancelMissedBillReqeustDTO.getOrderId())
                    .eq("shop_id", cancelMissedBillReqeustDTO.getShopId())
                    .eq("user_id", cancelMissedBillReqeustDTO.getUserId()))
                    .getOrderStatus();
            //如果该账单不为未接单状态,则不允许取消
            if (!orderStatus.equals(BillEnums.OrderStatus.Missed.index())) {
                throw new ServiceException("该账单状态不为未接单状态,无法取消!");
            }
            orderBillMapper.update(new OrderBill().setDr(true).setEndTime(new Date()), new UpdateWrapper<OrderBill>()
                    .eq("id", cancelMissedBillReqeustDTO.getOrderId())
                    .eq("shop_id", cancelMissedBillReqeustDTO.getShopId())
                    .eq("user_id", cancelMissedBillReqeustDTO.getUserId()));
        } catch (Exception e) {
            log.error("cancelorderBill failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("cancelorderBill failed" + e);
        }
    }

    @Override
    public void deleteCompletedBill(DeleteCompletedBillReqeustDTO deleteCompletedBillReqeustDTO) throws ServiceException {
        try {
            /**
             * TODO 1. 根据loginUser的user_id查询绑定的shopId列表包不包括前端shopId
             * 2.查询该订单是否为已完成的状态
             * 3.删除该用户在该店铺下的指定订单
             */
            Integer orderStatus = orderBillMapper.selectOne(new QueryWrapper<OrderBill>()
                    .select("order_status")
                    .eq("id", deleteCompletedBillReqeustDTO.getOrderId())
                    .eq("shop_id", deleteCompletedBillReqeustDTO.getShopId())
                    .eq("user_id", deleteCompletedBillReqeustDTO.getUserId()))
                    .getOrderStatus();
            //如果该账单不为已完成状态,则不允许删除
            if (!orderStatus.equals(BillEnums.OrderStatus.Completed.index())) {
                throw new ServiceException("该账单状态不为已完成状态,无法删除!");
            }
            orderBillMapper.update(new OrderBill().setDr(true).setEndTime(new Date()), new UpdateWrapper<OrderBill>()
                    .eq("id", deleteCompletedBillReqeustDTO.getOrderId())
                    .eq("shop_id", deleteCompletedBillReqeustDTO.getShopId())
                    .eq("user_id", deleteCompletedBillReqeustDTO.getUserId()));
        } catch (Exception e) {
            log.error("deleteCompletedBill failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("deleteCompletedBill failed" + e);
        }
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
            tmp = new BigDecimal(targetString);
        } catch (NumberFormatException e) {

        }
        if (tmp != null) {
            return true;
        }
        return false;
    }

}
