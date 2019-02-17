package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.dao.EggTypeMapper;
import com.zero.egg.model.EggType;
import com.zero.egg.requestDTO.EggTypeRequestDTO;
import com.zero.egg.responseDTO.EggTypeListResponseDTO;
import com.zero.egg.service.EggTypeService;
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
 * @ClassName BaseInfoServiceImpl
 * @Description 鸡蛋类型模块ServiceImpl
 * @Author lyming
 * @Date 2018/11/1 19:36
 **/
@Service
@Slf4j
public class EggTypeServiceImpl implements EggTypeService {


    @Autowired
    private EggTypeMapper eggTypeMapper;

    @Override
    @Transactional
    public Message saveEggType(EggTypeRequestDTO saveEggTypeRequestDTO) {
        EggType eggType = new EggType();
        Message message = new Message();
        /** 查重结果 */
        List<EggType> resultList = null;
        try {
            TransferUtil.copyProperties(eggType, saveEggTypeRequestDTO);
            /** 插入数据前做查重操作
             *  name不能重复
             */
            if (checkByName(resultList, eggType) > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
            } else {
                eggTypeMapper.insert(eggType);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("saveEggType Error!", e);
            throw new ServiceException("saveEggType Error!:" + e.toString());
        }
    }

    @Override
    public void deleteEggTypeById(EggTypeRequestDTO deleteEggTypeRequestDTO) {
        try {
            eggTypeMapper.delete(new QueryWrapper<EggType>()
                    .eq("id", deleteEggTypeRequestDTO.getId())
                    .eq("shop_id", deleteEggTypeRequestDTO.getShopId())
                    .eq("company_id", deleteEggTypeRequestDTO.getCompanyId()));
        } catch (Exception e) {
            log.error("deleteEggTypeById Error!", e);
            throw new ServiceException("deleteEggTypeById Error!");
        }
    }

    @Override
    public void batchDeleteEggType(EggTypeRequestDTO batchDeleteEggTypeRequestDTO) {
        try {
            eggTypeMapper.delete(new QueryWrapper<EggType>()
                    .in("id", batchDeleteEggTypeRequestDTO.getIds())
                    .eq("shop_id", batchDeleteEggTypeRequestDTO.getShopId())
                    .eq("company_id", batchDeleteEggTypeRequestDTO.getCompanyId()));
        } catch (Exception e) {
            log.error("batchDeleteEggType Error!", e);
            throw new ServiceException("batchDeleteEggType Error!");
        }
    }


    @Override
    public Message listEggType(EggTypeRequestDTO eggTypeRequestDTO) {
        /**
         * 如果前端传来了strEggTypeName,则认为是条件模糊查询
         * 否则默认列出全部鸡蛋类型
         */
        Message message = new Message();
        Page<EggType> page = new Page<>();
        EggTypeListResponseDTO eggTypeListResponseDTO = new EggTypeListResponseDTO();
        try {
            page.setCurrent(eggTypeRequestDTO.getCurrent());
            page.setSize(eggTypeRequestDTO.getSize());
            page = (Page<EggType>) eggTypeMapper.selectPage(page, new QueryWrapper<EggType>()
                    .eq("shop_id", eggTypeRequestDTO.getShopId())
                    .eq("company_id", eggTypeRequestDTO.getCompanyId())
                    .like(null != eggTypeRequestDTO.getName() && !"".equals(eggTypeRequestDTO.getName())
                            , "name", eggTypeRequestDTO.getName()));
            eggTypeListResponseDTO.setEggTypeList(page.getRecords());
            eggTypeListResponseDTO.setCurrent(page.getCurrent());
            eggTypeListResponseDTO.setPages(page.getPages());
            eggTypeListResponseDTO.setTotal(page.getTotal());
            eggTypeListResponseDTO.setSize(page.getSize());

            message.setData(eggTypeListResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("listEggType Error!", e);
            throw new ServiceException("listEggType Error!");
        }
        return message;
    }

    @Override
    @Transactional
    public Message modifyEggType(EggTypeRequestDTO modifyEggTypeRequestDTO) {
        Message message = new Message();
        EggType eggType = new EggType();
        /** 查重结果 */
        List<EggType> resultList = null;
        try {
            TransferUtil.copyProperties(eggType, modifyEggTypeRequestDTO);
            /** str_eggtype_name 不能已经存在 */
            if (checkByName(resultList, eggType) > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
            } else {
                eggTypeMapper.update(eggType, new UpdateWrapper<EggType>()
                        .eq("id", eggType.getId())
                        .eq("shop_id", eggType.getShopId())
                        .eq("company_id", eggType.getCompanyId()));
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.error("modifyEggType Error:" + e.toString());
            throw new ServiceException("modifyEggType Error");
        }
    }

    @Override
    public Message selectEggTypeById(EggTypeRequestDTO eggTypeRequestDTO) throws ServiceException {
        Message message = new Message();
        EggType eggType = null;
        try {
            eggType = eggTypeMapper.selectOne(new QueryWrapper<EggType>()
                    .eq("id", eggTypeRequestDTO.getId())
                    .eq("shop_id", eggTypeRequestDTO.getShopId())
                    .eq("company_id", eggTypeRequestDTO.getCompanyId()));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setData(eggType);
        } catch (Exception e) {
            log.error("selectEggTypeById Error!:" + e.toString());
            throw new ServiceException("selectEggTypeById Error!");
        }
        return message;
    }

    /**
     * 同个企业同个店铺只能存在独一无二的类型名
     *
     * @param resultList
     * @param eggType
     * @return 查重得到的集合大小
     */
    private int checkByName(List<EggType> resultList, EggType eggType) {
        resultList = eggTypeMapper.selectList(new QueryWrapper<EggType>()
                .eq("name", eggType.getName())
                .eq("shop_id", eggType.getShopId())
                .eq("company_id", eggType.getCompanyId()));
        return resultList.size();
    }
}
