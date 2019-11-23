package com.zero.egg.service;

import com.zero.egg.requestDTO.SpecificationProgramRequestDTO;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

/**
 * 方案Service
 *
 * @ClassName SpecificationProgramService
 * @Author lyming
 * @Date 2018/11/9 14:46
 **/
public interface SpecificationProgramService {

    /**
     * 新增方案
     *
     * @param specificationProgramRequestDTO
     * @return
     */
    public Message addStandardData(SpecificationProgramRequestDTO specificationProgramRequestDTO);

    /**
     * 根据id删除方案
     *
     * @Param [specificationProgramRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message deleteStandardDataById(SpecificationProgramRequestDTO specificationProgramRequestDTO);

    /**
     * 列出选中品种下的所有方案和方案细节
     *
     * @Param [specificationProgramRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message listDataAndDetl(SpecificationProgramRequestDTO specificationProgramRequestDTO);

    /**
     * 编辑方案(方案名)
     * @param specificationProgramRequestDTO
     * @return
     * @throws ServiceException
     */
    public Message updateSpecificationProgram(SpecificationProgramRequestDTO specificationProgramRequestDTO) throws ServiceException;

    /**
     * 根据品种id列出所有方案(下拉列表用)
     *
     * @param specificationProgramRequestDTO
     * @return
     */
    Message listData(SpecificationProgramRequestDTO specificationProgramRequestDTO) throws ServiceException;
}
