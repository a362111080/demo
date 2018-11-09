package com.zero.egg.service.impl;

import com.zero.egg.dao.StandardDetlMapper;
import com.zero.egg.model.StandardDetl;
import com.zero.egg.requestDTO.StandardDetlRequestDTO;
import com.zero.egg.service.StandardDetlService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 方案细节ServiceImpl
 *
 * @ClassName StandardDetlServiceImpl
 * @Author lyming
 * @Date 2018/11/9 17:04
 **/
@Service
@Slf4j
public class StandardDetlServiceImpl implements StandardDetlService {

    @Autowired
    private StandardDetlMapper standardDetlMapper;

    /**
     * 新增方案细节
     *
     * @param standardDetlRequestDTO
     * @return
     */
    @Override
    @Transactional
    public Message addStandardDetl(StandardDetlRequestDTO standardDetlRequestDTO) {
        Message message = new Message();
        StandardDetl standardDetl = new StandardDetl();
        try {
            TransferUtil.copyProperties(standardDetl, standardDetlRequestDTO);
            standardDetlMapper.insert(standardDetl);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("addStandardDetl error", e);
            throw new ServiceException("addStandardDetl error");

        }
    }

    /**
     * 更新方案细节
     *
     * @param standardDetlRequestDTO
     * @return
     */
    public Message updateStandardDetl(StandardDetlRequestDTO standardDetlRequestDTO) {
        Message message = new Message();
        StandardDetl standardDetl = new StandardDetl();
        try {
            TransferUtil.copyProperties(standardDetl, standardDetlRequestDTO);
            standardDetlMapper.updateById(standardDetl);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("updateStandardDetl error", e);
            throw new ServiceException("updateStandardDetl error");

        }
    }
}
