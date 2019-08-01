package com.zero.egg.service;

import com.zero.egg.requestDTO.QueryRetailDeatilsRequestDTO;
import com.zero.egg.requestDTO.SaveRetailDeatilsRequestDTO;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

public interface BdRetailService{
    /**
     * 查询指定零售订单的销售记录
     * @param queryRetailDeatilsRequestDTO
     * @return
     * @throws ServiceException
     */
    public Message listRetailDetails(QueryRetailDeatilsRequestDTO queryRetailDeatilsRequestDTO) throws ServiceException;

    /**
     * 新增零售销售记录
     * @param saveRetailDeatilsRequestDTO
     * @return
     * @throws ServiceException
     */
    public long insertRetailDetails(SaveRetailDeatilsRequestDTO saveRetailDeatilsRequestDTO) throws ServiceException;
}
