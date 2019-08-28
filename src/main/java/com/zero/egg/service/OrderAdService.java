package com.zero.egg.service;

import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

/**
 * @author lym
 */
public interface OrderAdService {

    /**
     * 获取广告列表
     * @return Message
     * @throws ServiceException
     */
    Message getAdList() throws ServiceException;
}
