package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.requestDTO.StandardDataRequestDTO;
import com.zero.egg.dao.StandardDataMapper;
import com.zero.egg.model.StandardData;
import com.zero.egg.service.StandardDataService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 方案ServiceImpl
 *
 * @ClassName StandardDataServiceImpl
 * @Author lyming
 * @Date 2018/11/9 14:52
 **/
@Service
@Slf4j
public class StandardDataServiceImpl implements StandardDataService {

    @Autowired
    private StandardDataMapper standardDataMapper;

    @Override
    @Transactional
    public Message addStandardData(StandardDataRequestDTO standardDataRequestDTO) {
        Message message = new Message();
        StandardData standardData = new StandardData();
        /**
         * 查重结果
         */
        List<StandardData> resultList = null;
        try {
            TransferUtil.copyProperties(standardData, standardDataRequestDTO);
            /**
             * strStandName,strStandCode 不能重复
             */
            resultList = standardDataMapper.selectList(new QueryWrapper<StandardData>()
                    .eq("strStandName", standardData.getStrStandName())
                    .or()
                    .eq("strStandCode", standardData.getStrStandCode()));
            if (null != resultList) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
            } else {
                standardDataMapper.insert(standardData);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.info("addStandardData error", e);
            throw new ServiceException("addStandardData error");
        }
    }
}
