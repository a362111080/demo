package com.zero.egg.service;

import com.zero.egg.requestDTO.StandardDataRequestDTO;
import com.zero.egg.tool.Message;

/**
 * 方案Service
 *
 * @ClassName StandardDataService
 * @Author lyming
 * @Date 2018/11/9 14:46
 **/
public interface StandardDataService {

    /**
     * 新增方案
     *
     * @param standardDataRequestDTO
     * @return
     */
    public Message addStandardData(StandardDataRequestDTO standardDataRequestDTO);

    /**
     * 根据id删除方案
     *
     * @Param [standardDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message deleteStandardDataById(StandardDataRequestDTO standardDataRequestDTO);

    /**
     * 列出选中品种下的所有方案和方案细节
     *
     * @Param [standardDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message listDataAndDetl(StandardDataRequestDTO standardDataRequestDTO);
}
