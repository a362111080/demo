package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.BdRetailMapper;
import com.zero.egg.dao.CustomerMapper;
import com.zero.egg.model.BdRetail;
import com.zero.egg.model.Customer;
import com.zero.egg.requestDTO.QueryRetailDeatilsRequestDTO;
import com.zero.egg.service.BdRetailService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BdRetailServiceImpl implements BdRetailService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private BdRetailMapper bdRetailMapper;

    @Override
    public Message listRetailDetails(QueryRetailDeatilsRequestDTO queryRetailDeatilsRequestDTO) throws ServiceException {
        Message message = new Message();
        try {
            //零售商验证
            Integer isRetail = customerMapper.selectOne(new QueryWrapper<Customer>()
                    .select("is_retail")
                    .eq("id", queryRetailDeatilsRequestDTO.getCustomerId())
                    .eq("company_id", queryRetailDeatilsRequestDTO.getCompanyId())
                    .eq("shop_id", queryRetailDeatilsRequestDTO.getShopId())
                    .eq("dr", 0))
                    .getIsRetail();
            if (0 == isRetail) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NOT_RETAIL_CUSTOMER);
                return message;
            }
            //查询零售记录,按时间顺序升序查看
            List<BdRetail> retailList = bdRetailMapper.selectList(new QueryWrapper<BdRetail>()
                    .select("quantity,quantity_mode,amount,createtime")
                    .eq("bill_id", queryRetailDeatilsRequestDTO.getBillId())
                    .eq("company_id", queryRetailDeatilsRequestDTO.getCompanyId())
                    .eq("shop_id", queryRetailDeatilsRequestDTO.getShopId())
                    .eq("dr", 0)
                    .orderByAsc("createtime"));
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            message.setData(retailList);
            return message;
        } catch (Exception e) {
            log.error("listRetailDetails service error:" + e);
            throw new ServiceException("listRetailDetails service error");
        }
    }
}
