package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.egg.dao.SpecificationMapper;
import com.zero.egg.dao.SpecificationProgramMapper;
import com.zero.egg.model.Specification;
import com.zero.egg.model.SpecificationProgram;
import com.zero.egg.requestDTO.SpecificationProgramRequestDTO;
import com.zero.egg.responseDTO.SpecificationProgramListResponseDTO;
import com.zero.egg.responseDTO.SpecificationProgramResponseDTO;
import com.zero.egg.service.SpecificationProgramService;
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
 * @ClassName SpecificationProgramServiceImpl
 * @Author lyming
 * @Date 2018/11/9 14:52
 **/
@Service
@Slf4j
public class SpecificationProgramServiceImpl implements SpecificationProgramService {

    @Autowired
    private SpecificationProgramMapper specificationProgramMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Override
    @Transactional
    public Message addStandardData(SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = new Message();
        SpecificationProgram specificationProgram = new SpecificationProgram();
        /**
         * 查重结果
         */
        List<SpecificationProgram> resultList = null;
        try {
            TransferUtil.copyProperties(specificationProgram, specificationProgramRequestDTO);
            /** 查重 */
            if (checkByName(resultList, specificationProgram) > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
            } else {
                specificationProgramMapper.insert(specificationProgram);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.info("addStandardData error", e.toString());
            throw new ServiceException("addStandardData error");
        }
    }

    /**
     * 根据id删除方案
     *
     * @param specificationProgramRequestDTO
     * @return
     */
    @Override
    @Transactional
    public Message deleteStandardDataById(SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = new Message();
        SpecificationProgram specificationProgram = new SpecificationProgram();
        try {
            TransferUtil.copyProperties(specificationProgram, specificationProgramRequestDTO);
            specificationProgram.setDr(1);
            /**
             * 1. 查询方案下相关联的方案细节,并逻辑删除
             */
            List<Specification> specificationList = specificationMapper.selectList(new QueryWrapper<Specification>()
                    .eq("program_id", specificationProgramRequestDTO.getId())
                    .eq("shop_id", specificationProgramRequestDTO.getShopId())
                    .eq("company_id", specificationProgramRequestDTO.getCompanyId()));
            for (Specification specification : specificationList) {
                specification.setDr(1);
                specificationMapper.update(specification, new UpdateWrapper<Specification>()
                        .eq("id", specification.getId())
                        .eq("shop_id", specification.getShopId())
                        .eq("company_id", specification.getCompanyId()));
            }
            /**
             * 2.逻辑删除该方案
             */
            specificationProgramMapper.update(specificationProgram, new UpdateWrapper<SpecificationProgram>()
                    .eq("id", specificationProgram.getId())
                    .eq("shop_id", specificationProgram.getShopId())
                    .eq("company_id", specificationProgram.getCompanyId()));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("deleteStandardDataById error", e.toString());
            throw new ServiceException("deleteStandardDataById error");
        }
    }

    /**
     * 列出选中品种下的所有方案和方案细节
     * 暂不需要分页处理
     *
     * @Param [specificationProgramRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    @Override
    public Message listDataAndDetl(SpecificationProgramRequestDTO specificationProgramRequestDTO) {
        Message message = new Message();
        List<SpecificationProgram> specificationProgramList;
        List<Specification> specificationList;
        SpecificationProgramListResponseDTO specificationProgramListResponseDTO = null;
        SpecificationProgramResponseDTO specificationProgramResponseDTO = new SpecificationProgramResponseDTO();
        List<SpecificationProgramListResponseDTO> specificationProgramListResponseDTOList = new ArrayList<>();
        /**
         * ---specificationProgramResponseDTO
         *    ---specificationProgramListResponseDTOList
         *          ---standardDataListResponseDTO1
         *                ---specificationList
         *                      ---StandardDetl1
         *                      ---StandardDetl2
         *          ---standardDataListResponseDTO2
         *                ---specificationList
         *                      ---StandardDetl1
         *                      ---StandardDetl2
         */
        try {
            /**
             * 1.先根据category_id查询出所有方案
             * 2.根据program_id查出所有方案细节
             */
            specificationProgramList = specificationProgramMapper.selectList(new QueryWrapper<SpecificationProgram>()
                    .eq("category_id", specificationProgramRequestDTO.getCategoryId())
                    .eq("dr", 0)
                    .eq("shop_id", specificationProgramRequestDTO.getShopId())
                    .eq("company_id", specificationProgramRequestDTO.getCompanyId()));
            for (SpecificationProgram specificationProgram : specificationProgramList) {
                specificationProgramListResponseDTO = new SpecificationProgramListResponseDTO();
                specificationList = specificationMapper.selectList(new QueryWrapper<Specification>()
                        .eq("program_id", specificationProgram.getId())
                        .eq("dr", 0)
                        .eq("shop_id", specificationProgram.getShopId())
                        .eq("company_id", specificationProgram.getCompanyId())
                        .orderByAsc("weight_min"));
                TransferUtil.copyProperties(specificationProgramListResponseDTO, specificationProgram);
                specificationProgramListResponseDTO.setSpecificationList(specificationList);
                specificationProgramListResponseDTOList.add(specificationProgramListResponseDTO);
            }
            specificationProgramResponseDTO.setStandardDataList(specificationProgramListResponseDTOList);
            message.setData(specificationProgramResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("listDataAndDetl error", e.toString());
            throw new ServiceException("listDataAndDetl error");
        }
    }

    @Override
    @Transactional
    public Message updateSpecificationProgram(SpecificationProgramRequestDTO specificationProgramRequestDTO) throws ServiceException {
        Message message = new Message();
        SpecificationProgram specificationProgram = new SpecificationProgram();
        /**
         * 查重结果
         */
        List<SpecificationProgram> resultList = null;
        try {
            TransferUtil.copyProperties(specificationProgram, specificationProgramRequestDTO);
            /**
             * 如果前端传来的新的方案名不为null或者空字符串,则判断新的方案名是否重复,若不重复,则更新
             */
            if (null != specificationProgramRequestDTO.getName() && !"".equals(specificationProgramRequestDTO.getName())) {
                /**
                 * 查重
                 */
                if (checkByName(resultList, specificationProgram) > 0) {
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
                } else {
                    specificationProgramMapper.updateById(specificationProgram);
                    message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                }
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
            }
        } catch (Exception e) {
            log.info("updateSpecificationProgram error", e.toString());
            throw new ServiceException("updateSpecificationProgram error");
        }
        return message;
    }

    @Override
    public Message listData(SpecificationProgramRequestDTO specificationProgramRequestDTO) throws ServiceException {
        Message message = new Message();
        List<SpecificationProgram> specificationProgramList;
        try {
            specificationProgramList = specificationProgramMapper.selectList(new QueryWrapper<SpecificationProgram>()
                    .eq("category_id", specificationProgramRequestDTO.getCategoryId())
                    .eq("dr", 0)
                    .eq("shop_id", specificationProgramRequestDTO.getShopId())
                    .eq("company_id", specificationProgramRequestDTO.getCompanyId()));
            message.setData(specificationProgramList);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("listDataAndDetl error", e.toString());
            throw new ServiceException("listDataAndDetl error");
        }
    }


    /**
     * 同个企业同个店铺同一种类型只能存在独一无二的方案名
     *
     * @param resultList
     * @param specificationProgram
     * @return 查重得到的集合大小
     */
    private int checkByName(List<SpecificationProgram> resultList, SpecificationProgram specificationProgram) {
        resultList = specificationProgramMapper.selectList(new QueryWrapper<SpecificationProgram>()
                .eq("category_id", specificationProgram.getCategoryId())
                .eq("name", specificationProgram.getName())
                .eq("shop_id", specificationProgram.getShopId())
                .eq("company_id", specificationProgram.getCompanyId())
                .eq("dr", 0));
        return resultList.size();
    }
}
