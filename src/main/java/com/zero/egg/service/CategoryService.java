package com.zero.egg.service;

import com.zero.egg.requestDTO.CategoryRequestDTO;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

/**
 * 鸡蛋类型Service
 *
 * @Author lyming
 * @Date 2018/11/1 19:25
 **/
public interface CategoryService {

    /**
     * 新增鸡蛋类型
     *
     * @Param [eggType]
     * @Return void
     **/
    public Message saveEggType(CategoryRequestDTO saveCategoryRequestDTO) throws ServiceException;

    /**
     * 根据id删除鸡蛋类型
     *
     * @Param [eggType]
     * @Return void
     **/
    public void deleteEggTypeById(CategoryRequestDTO deleteCategoryRequestDTO) throws ServiceException;

    /**
     * 批量删除鸡蛋类型
     *
     * @Param [eggType]
     * @Return void
     **/
    public void batchDeleteEggType(CategoryRequestDTO batchDeleteCategoryRequestDTO) throws ServiceException;

    /**
     * 鸡蛋类型列表
     *
     * @Param [eggType]
     * @Return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.zero.egg.model.Category>
     **/
    public Message listEggType(CategoryRequestDTO categoryRequestDTO) throws ServiceException;

    /**
     * 修改鸡蛋类型
     *
     * @param modifyCategoryRequestDTO
     * @return
     */
    public Message modifyEggType(CategoryRequestDTO modifyCategoryRequestDTO) throws ServiceException;

    /**
     * 按主键查询品种接口
     *
     * @param categoryRequestDTO
     * @return
     * @throws ServiceException
     */
    public Message selectEggTypeById(CategoryRequestDTO categoryRequestDTO) throws ServiceException;
}
