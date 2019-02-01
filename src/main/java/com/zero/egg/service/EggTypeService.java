package com.zero.egg.service;

import com.zero.egg.requestDTO.EggTypeRequestDTO;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

/**
 * 鸡蛋类型Service
 *
 * @Author lyming
 * @Date 2018/11/1 19:25
 **/
public interface EggTypeService {

    /**
     * 新增鸡蛋类型
     *
     * @Param [eggType]
     * @Return void
     **/
    public Message saveEggType(EggTypeRequestDTO saveEggTypeRequestDTO) throws ServiceException;

    /**
     * 根据id删除鸡蛋类型
     *
     * @Param [eggType]
     * @Return void
     **/
    public void deleteEggTypeById(EggTypeRequestDTO deleteEggTypeRequestDTO) throws ServiceException;

    /**
     * 批量删除鸡蛋类型
     *
     * @Param [eggType]
     * @Return void
     **/
    public void batchDeleteEggType(EggTypeRequestDTO batchDeleteEggTypeRequestDTO) throws ServiceException;

    /**
     * 鸡蛋类型列表
     *
     * @Param [eggType]
     * @Return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.zero.egg.model.EggType>
     **/
    public Message listEggType(EggTypeRequestDTO eggTypeRequestDTO) throws ServiceException;

    /**
     * 修改鸡蛋类型
     *
     * @param modifyEggTypeRequestDTO
     * @return
     */
    public Message modifyEggType(EggTypeRequestDTO modifyEggTypeRequestDTO) throws ServiceException;
}
