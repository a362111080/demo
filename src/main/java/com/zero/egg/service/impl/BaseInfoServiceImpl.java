package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.RequestDTO.EggTypeRequestDTO;
import com.zero.egg.ResponseDTO.EggTypeListResponseDTO;
import com.zero.egg.dao.EggTypeMapper;
import com.zero.egg.model.EggType;
import com.zero.egg.service.BaseInfoService;
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
 * @Description 基础信息模块ServiceImpl
 * @Author lyming
 * @Date 2018/11/1 19:36
 **/
@Service
@Slf4j
public class BaseInfoServiceImpl implements BaseInfoService {


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
             *  name和code不能重复
             */
            resultList = eggTypeMapper.selectList(new QueryWrapper<EggType>()
                    .eq("strEggTypeName", eggType.getStrEggTypeName())
                    .or()
                    .eq("strEggTypeCode", eggType.getStrEggTypeCode()));
            if (null != resultList) {
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
            throw new ServiceException("saveEggType Error!");
        }
    }

    @Override
    public void deleteEggTypeById(EggTypeRequestDTO deleteEggTypeRequestDTO) {
        try {
            eggTypeMapper.deleteById(deleteEggTypeRequestDTO.getId());
        } catch (Exception e) {
            log.error("deleteEggTypeById Error!", e);
            throw new ServiceException("deleteEggTypeById Error!");
        }
    }

    public void batchDeleteEggType(EggTypeRequestDTO batchDeleteEggTypeRequestDTO) {
        try {
            eggTypeMapper.deleteBatchIds(batchDeleteEggTypeRequestDTO.getIds());
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
            if (null != eggTypeRequestDTO.getStrEggTypeName() && !"".equals(eggTypeRequestDTO.getStrEggTypeName())) {
                page = (Page<EggType>) eggTypeMapper.selectPage(page, new QueryWrapper<EggType>()
                        .like("strEggTypeName", eggTypeRequestDTO.getStrEggTypeName())
                );
            } else {
                page = (Page<EggType>) eggTypeMapper.selectPage(page, null);
            }
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
}
