package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zero.egg.dao.BarCodeMapper;
import com.zero.egg.dao.SupplierMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.service.SupplierService;
import com.zero.egg.tool.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

;

/**
 * @ClassName SupplierServiceImpl
 * @Description 合作单位模块ServiceImpl
 * @Author CQ
 * @Date 2018/11/7
 **/
@Service
@Transactional(rollbackFor=Exception.class)
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper mapper;

    @Autowired
    private BarCodeMapper barCodeMapper;

    @Override
    public int AddSupplier(Supplier model) {
        return mapper.insert(model);
    }

    @Override
    public int UpdateSupplier(Supplier model) {
        try {
            //状态 1:停用 0:启用
            //原本的合作状态
            String status = mapper.selectOne(new QueryWrapper<Supplier>()
                    .select("status")
                    .eq("id", model.getId()))
                    .getStatus();
            //如果前后状态相同,对二维码不做任何操作
            //如果1-->0 要启用二维码
            //如果0-->1 要停用二维码
            BarCode barCode;
            if ("0".equals(status) && "1".equals(model.getStatus())) {
                barCode = new BarCode().setDr(true);
                barCodeMapper.update(barCode, new UpdateWrapper<BarCode>()
                        .eq("supplier_id", model.getId()));
            }
            if ("1".equals(status) && "0".equals(model.getStatus())) {
                barCode = new BarCode().setDr(false);
                barCodeMapper.update(barCode, new UpdateWrapper<BarCode>()
                        .eq("supplier_id", model.getId()));
            }
            int updateNum = mapper.UpdateSupplier(model);
            //如果需要更新供应商名,那二维码的供应商也要跟着变
            barCodeMapper.update(new BarCode().setSupplierName(model.getName()), new UpdateWrapper<BarCode>()
                    .eq("supplier_id", model.getId()));
            return updateNum;
        } catch (Exception e) {
            log.error("UpdateSupplier failed" + e);
            throw new ServiceException("UpdateSupplier failed" + e);
        }
    }

    @Override
    public List<Supplier> GetSupplierList(SupplierRequestDTO model) {
        return mapper.GetSupplierList(model);
    }

    @Override
    public int DeleteSupplier(SupplierRequestDTO supplier) {
        try {
            /* 删除合作商之前要逻辑删除应对的二维码信息 */
            List<String> ids = supplier.getIds();
            BarCode barCode = new BarCode();
            barCode.setDr(true);
            for (String id : ids) {
                barCodeMapper.update(barCode, new UpdateWrapper<BarCode>().eq("supplier_id", id));
            }
            return mapper.DeleteSupplier(ids);
        } catch (Exception e) {
            log.error("DeleteSupplier error:" + e);
            throw new ServiceException("DeleteSupplier Service error");
        }
    }

    @Override
    public int queryCountByCode(String code) {
        int count = mapper.selectCount(new QueryWrapper<Supplier>()
                .eq("code", code));
        return count;
    }

}
