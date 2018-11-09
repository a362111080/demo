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
}
