package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.egg.dao.SpecificationMapper;
import com.zero.egg.model.Specification;
import com.zero.egg.requestDTO.SpecificationRequestDTO;
import com.zero.egg.responseDTO.SpecificationResponseDTO;
import com.zero.egg.service.SpecificationService;
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
 * 方案细节ServiceImpl
 *
 * @ClassName SpecificationServiceImpl
 * @Author lyming
 * @Date 2018/11/9 17:04
 **/
@Service
@Slf4j
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    /**
     * 新增方案细节
     *
     * @param specificationRequestDTO
     * @return
     */
    @Override
    public Message addStandardDetl(SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        Specification specification = new Specification();
        try {
            TransferUtil.copyProperties(specification, specificationRequestDTO);
            /**
             * 如果计重方式是包,则不计重
             */
            if (2 == specification.getMode()) {
                specification.setNumerical(null);
            }
            /**
             * 如果传入的最小重量大于最大重量,则返回错误信息
             */
            if ((specification.getWeightMin().compareTo(specification.getWeightMax())) > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
            } else {
                specificationMapper.insert(specification);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.info("addStandardDetl error", e);
            throw new ServiceException("addStandardDetl error");
        }
    }

    /**
     * 更新方案细节
     *
     * @param specificationRequestDTO
     * @return
     */
    public Message updateStandardDetl(SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        Specification specification = new Specification();
        try {
            TransferUtil.copyProperties(specification, specificationRequestDTO);
            /**
             * 如果计重方式是包,则不计重
             */
            if (2 == specification.getMode()) {
                specification.setNumerical(null);
            }
            /**
             * 如果传入的最小重量大于最大重量,则返回错误信息
             */
            if ((specification.getWeightMin().compareTo(specification.getWeightMax())) > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
            } else {
                specificationMapper.updateById(specification);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.info("updateStandardDetl error", e);
            throw new ServiceException("updateStandardDetl error");

        }
    }

    /**
     * 批量删除方案细节
     *
     * @param specificationRequestDTO
     * @return
     */
    @Override
    public Message batchDeleteStandardDetl(SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        Specification specification = new Specification();
        try {
//            specificationMapper.deleteBatchIds(specificationRequestDTO.getIds());
            /**
             * 只做逻辑删除
             */
            specification.setDr(1);
            specificationMapper.update(specification, new UpdateWrapper<Specification>()
                    .in("id", specificationRequestDTO.getIds()));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("updateStandardDetl error", e);
            throw new ServiceException("updateStandardDetl error");
        }
    }

    /**
     * 根据program_id(方案id)列出所属方案细节
     * 不用分页
     *
     * @param specificationRequestDTO
     * @return
     */
    @Override
    public Message listStandardDetlByProgramId(SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        SpecificationResponseDTO specificationResponseDTO = new SpecificationResponseDTO();
        List<Specification> specificationList = null;
        try {
            specificationList = specificationMapper.selectList(new QueryWrapper<Specification>()
                    .eq("program_id", specificationRequestDTO.getProgramId())
                    .eq("dr", 0));
            specificationResponseDTO.setSpecificationList(specificationList);
            message.setData(specificationResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("listStandardDetlByStandDetlCode error", e);
            throw new ServiceException("listStandardDetlByStandDetlCode error");
        }
    }
}