package com.zero.egg.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zero.egg.model.OrderBill;
import com.zero.egg.model.OrderBillDetail;
import com.zero.egg.model.Task;
import com.zero.egg.requestDTO.*;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

import java.util.List;

/**
 * 订货平台订单Service
 * @author lym
 */
public interface OrderBillService {

    /**
     * 新增一条订货账单记录
     * @param addOrderBillRequestDTO
     * @throws ServiceException
     */
    Message addNewBill(AddOrderBillRequestDTO addOrderBillRequestDTO) throws ServiceException;

    /**
     * 订货平台订单列表
     * @param orderBillListReqeustDTO
     * @return
     * @throws ServiceException
     */
    IPage listOrderBill(OrderBillListReqeustDTO orderBillListReqeustDTO) throws ServiceException;


    /**
     * 取消未接单的订单
     * @throws ServiceException
     */
    void cancelorderBill(CancelMissedBillReqeustDTO cancelMissedBillReqeustDTO ) throws ServiceException;

    /**
     * 删除已经完成的的订单
     * @throws ServiceException
     */
    void deleteCompletedBill(DeleteCompletedBillReqeustDTO deleteCompletedBillReqeustDTO) throws ServiceException;

    /**
     * 获取订货平台订单详情
     * @param orderBillDetailsRequestDTO
     * @return
     * @throws ServiceException
     */
    Message getOrderBillDetails(OrderBillDetailsRequestDTO orderBillDetailsRequestDTO) throws ServiceException;

    void editorderstatus(OrderBill model);

    List<OrderBill> queryshoporder(OrderGoodsRequestDTO model);

    List<OrderBillDetail> GetOrderGoodDelList(OrderBill orderBill);

    List<OrderBill> queryshipmentorder(Task model);
}
