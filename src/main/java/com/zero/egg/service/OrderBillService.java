package com.zero.egg.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zero.egg.requestDTO.AddOrderBillRequestDTO;
import com.zero.egg.requestDTO.CancelMissedBillReqeustDTO;
import com.zero.egg.requestDTO.OrderBillListReqeustDTO;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

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
}
