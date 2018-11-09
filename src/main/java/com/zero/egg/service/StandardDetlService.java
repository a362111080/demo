package com.zero.egg.service;

import com.zero.egg.requestDTO.StandardDetlRequestDTO;
import com.zero.egg.tool.Message;

/**
 * 方案细节Service
 *
 * @Author lyming
 * @Date 2018/11/9 17:01
 **/
public interface StandardDetlService {

    /**
     * 新增一条方案细节
     *
     * @Param [standardDetlRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message addStandardDetl(StandardDetlRequestDTO standardDetlRequestDTO);
}
