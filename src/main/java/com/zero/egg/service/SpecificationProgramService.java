package com.zero.egg.service;

import com.zero.egg.requestDTO.SpecificationProgramRequestDTO;
import com.zero.egg.tool.Message;

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
}
