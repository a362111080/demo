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

    /**
     * 修改方案细节
     *
     * @Param [standardDetlRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message updateStandardDetl(StandardDetlRequestDTO standardDetlRequestDTO);

    /**
     * 批量删除方案细节
     *
     * @Param [standardDetlRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message batchDeleteStandardDetl(StandardDetlRequestDTO standardDetlRequestDTO);

    /**
     * 根据StandDetlCode列出所属方案细节
     *
     * @Param [standardDetlRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message listStandardDetlByStandDetlCode(StandardDetlRequestDTO standardDetlRequestDTO);
}
