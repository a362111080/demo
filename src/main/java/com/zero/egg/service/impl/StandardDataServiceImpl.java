package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.StandardDataMapper;
import com.zero.egg.dao.StandardDetlMapper;
import com.zero.egg.model.StandardData;
import com.zero.egg.model.StandardDetl;
import com.zero.egg.requestDTO.StandardDataRequestDTO;
import com.zero.egg.responseDTO.StandardDataListResponseDTO;
import com.zero.egg.responseDTO.StandardDataResponseDTO;
import com.zero.egg.service.StandardDataService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private StandardDetlMapper standardDetlMapper;

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
            if (resultList.size() > 0) {
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

    /**
     * 根据id删除方案
     *
     * @param standardDataRequestDTO
     * @return
     */
    @Override
    @Transactional
    public Message deleteStandardDataById(StandardDataRequestDTO standardDataRequestDTO) {
        Message message = new Message();
        try {
            /**
             * 1.先根据strStandId/strStandCode 删除所有所属方案细节
             * 2.根据id删除方案
             */
            standardDetlMapper.delete(new QueryWrapper<StandardDetl>().eq("strStandDetlCode", standardDataRequestDTO.getStrEggTypeCode()));
            standardDataMapper.delete(new QueryWrapper<StandardData>().eq("id", standardDataRequestDTO.getId()));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("deleteStandardDataById error", e);
            throw new ServiceException("deleteStandardDataById error");
        }
    }

    /**
     * 列出选中品种下的所有方案和方案细节
     * 暂不需要分页处理
     *
     * @Param [standardDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @Override
    public Message listDataAndDetl(StandardDataRequestDTO standardDataRequestDTO) {
        Message message = new Message();
        List<StandardData> standardDataList;
        List<StandardDetl> standardDetlList;
        StandardDataListResponseDTO standardDataListResponseDTO = new StandardDataListResponseDTO();
        StandardDataResponseDTO standardDataResponseDTO = new StandardDataResponseDTO();
        List<StandardDataListResponseDTO> standardDataListResponseDTOList = new ArrayList<>();
        try {
            /**
             * 1.先根据strEggTypeId/strEggTypeCode 查询出所有方案
             * 2.根据strStandId/strStandCode 查出所有方案细节
             */
            standardDataList = standardDataMapper.selectList(new QueryWrapper<StandardData>().eq("strEggTypeCode", standardDataRequestDTO.getStrEggTypeCode()));
            for (StandardData standardData : standardDataList) {
                standardDetlList = standardDetlMapper.selectList(new QueryWrapper<StandardDetl>().eq("strStandCode", standardData.getStrStandCode()));
                standardDataListResponseDTO.setStandardDetlList(standardDetlList);
                TransferUtil.copyProperties(standardDataListResponseDTO, standardData);
                standardDataListResponseDTOList.add(standardDataListResponseDTO);
            }
            standardDataResponseDTO.setStandardDataList(standardDataListResponseDTOList);
            message.setData(standardDataResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("listDataAndDetl error", e);
            throw new ServiceException("listDataAndDetl error");
        }
    }
}
