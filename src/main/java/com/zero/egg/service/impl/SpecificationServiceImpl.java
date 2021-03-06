package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.egg.dao.SpecificationMapper;
import com.zero.egg.dao.UnloadGoodsMapper;
import com.zero.egg.model.Specification;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.SaveSpecificationRequestDTO;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private UnloadGoodsMapper unloadGoodsMapper;

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
                specification.setNumerical(BigDecimal.ZERO);
            }
            /**
             * 如果传入的最小重量大于最大重量,则返回错误信息
             */
            if ((specification.getWeightMin().compareTo(specification.getWeightMax())) > 0) {
                throw new ServiceException("WeightMin>WeightMax");
            } else {
                specification.setCreatetime(new Date());
                specification.setWeightName("实重(" + specification.getWeightMin() + "~" + specification.getWeightMax() + ")");
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
    @Override
    public Message updateStandardDetl(SpecificationRequestDTO specificationRequestDTO) {
        Message message = new Message();
        Specification specification = new Specification();
        try {
            TransferUtil.copyProperties(specification, specificationRequestDTO);
            /**
             * 如果方案被使用过,则不能被修改
             */
            Integer existFlag = unloadGoodsMapper.SpecificationIsUsed(specification);
            if (existFlag !=null) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SPECIFICATION_USED);
                return message;
            }
            /**
             * 如果计重方式是包,则不计重
             */
            if (2 == specification.getMode()) {
                specification.setNumerical(BigDecimal.ZERO);
            }
            /**
             * 如果传入的最小重量大于最大重量,则返回错误信息
             */
            if ((specification.getWeightMin().compareTo(specification.getWeightMax())) > 0) {
                throw new ServiceException("WeightMin>WeightMax");
            } else {
                specification.setWeightName("实重(" + specification.getWeightMin() + "~" + specification.getWeightMax() + ")");
                specification.setModifytime(new Date());
                specification.setDr(0);
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
            specification.setModifytime(new Date());
            specification.setModifier(specificationRequestDTO.getModifier());
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
                    .eq("dr", 0)
                    .eq("shop_id", specificationRequestDTO.getShopId())
                    .eq("company_id", specificationRequestDTO.getCompanyId())
                    .orderByAsc("weight_min"));
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

    @Override
    public List<String> listStandardDetlIDsByProgramId(String programId, String companyId, String shopId) {
        Message message = new Message();
        List<String> ids = new ArrayList<>();
        List<Specification> specificationList = null;
        try {
            specificationList = specificationMapper.selectList(new QueryWrapper<Specification>()
                    .select("id")
                    .eq("program_id", programId)
                    .eq("dr", 0)
                    .eq("shop_id", shopId)
                    .eq("company_id", companyId));
            for (Specification specification : specificationList) {
                ids.add(specification.getId());
            }
            return ids;
        } catch (Exception e) {
            log.info("listStandardDetlByStandDetlCode error", e);
            throw new ServiceException("listStandardDetlByStandDetlCode error");
        }
    }

    @Override
    public Specification getById(Specification specification) {
        return specificationMapper.selectById(specification.getId());
    }

    @Override
    public void batchDeleteStandardDetlByIds(List<String> ids) {
        Specification specification = new Specification();
        try {
            /**
             * 只做逻辑删除
             */
            specification.setDr(1);
            specificationMapper.update(specification, new UpdateWrapper<Specification>()
                    .in("id", ids));
        } catch (Exception e) {
            log.info("batchDeleteStandardDetlByIds error", e);
            throw new ServiceException("batchDeleteStandardDetlByIds error");
        }
    }

    @Override
    public Message saveSpecification(SaveSpecificationRequestDTO saveSpecificationRequestDTO, LoginUser user) {
        Message message = new Message();
        try {
            String name = user.getName();
            //获取当前方案下所有细节id集合并置为删除状态
            List<String> ids = this.listStandardDetlIDsByProgramId(saveSpecificationRequestDTO
                    .getSpecificationRequestDTOS().get(0).getProgramId(), user.getCompanyId(), user.getShopId());
            /** 如果该方案下细节书不为null**/
            if (null != ids && 0 < ids.size()) {
                this.batchDeleteStandardDetlByIds(ids);
            }
            /* 迭代方案细节列表,将店铺id和企业id赋予给方案细节,并判断方案细节的id是否为空,为空走新增流程,否则走编辑 */
            List<SpecificationRequestDTO> dtoList = saveSpecificationRequestDTO.getSpecificationRequestDTOS();
            for (SpecificationRequestDTO requestDTO : dtoList) {
                requestDTO.setCompanyId(user.getCompanyId());
                requestDTO.setShopId(user.getShopId());
                requestDTO.setModifier(name);
                if (null != requestDTO.getId() && !"".equals(requestDTO.getId())) {
                    //编辑
                    message = this.updateStandardDetl(requestDTO);
                } else {
                    requestDTO.setCreator(name);
                    message = this.addStandardDetl(requestDTO);
                }
                if (message.getState() == UtilConstants.ResponseCode.EXCEPTION_HEAD) {
                    throw new ServiceException(message.getMessage());
                }
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("saveSpecification failed:" + e);
            throw new ServiceException(message.getMessage());
        }
    }
}
