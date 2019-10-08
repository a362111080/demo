package com.zero.egg.service.impl;

import com.zero.egg.dao.OrderBiilDetailMapper;
import com.zero.egg.dao.OrderBillMapper;
import com.zero.egg.requestDTO.AddOrderBillRequestDTO;
import com.zero.egg.service.OrderBillService;
import com.zero.egg.tool.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lym
 */
@Slf4j
@Service
public class OrderBillServiceImpl implements OrderBillService {

    @Autowired
    private OrderBillMapper orderBillMapper;

    @Autowired
    private OrderBiilDetailMapper orderBillDetailMapper;

    @Override
    public void addNewBill(AddOrderBillRequestDTO addOrderBillRequestDTO) throws ServiceException {
        try {
            /**
             * TODO 验证登录用户绑定的shopIdList中是否有传过来的shopId
             * 1.从addOrderBillRequestDTO中获取addressId,,进而获取地址信息,检验地址有效性(是否未删除,是否是该登录用户的地址),并封装到OrderBill对象中
             * 2.从addOrderBillRequestDTO中获取cartId列表,进而获取购物车商品列表信息,检验有效性(是否未删除,是否是该登录用户的购物车商品)
             * 3.从购物车商品信息中获取商品id,进而获取商品类别信息,同时将规格信息封装到OrderBillDetail对象中
             * 4.遍历购物车商品列表,计算小计金额,如果商品单价有一个为null,则OrderBill对象中的总计为null,否则叠加小计金额到总计
             * 5.删除购物车商品信息
             */
        } catch (Exception e) {
            log.error("addNewBill failed" + e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new ServiceException("addNewBill failed" + e);
        }
    }
}
