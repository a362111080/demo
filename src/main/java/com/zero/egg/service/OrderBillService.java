package com.zero.egg.service;


import com.zero.egg.requestDTO.AddOrderBillRequestDTO;
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
    void addNewBill(AddOrderBillRequestDTO addOrderBillRequestDTO) throws ServiceException;
}
