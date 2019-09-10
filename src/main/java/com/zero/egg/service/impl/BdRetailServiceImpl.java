package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.BillMapper;
import com.zero.egg.dao.CustomerMapper;
import com.zero.egg.dao.RetailMapper;
import com.zero.egg.model.Bill;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Retail;
import com.zero.egg.requestDTO.QueryRetailDeatilsRequestDTO;
import com.zero.egg.requestDTO.SaveRetailDeatilsRequestDTO;
import com.zero.egg.service.BdRetailService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class BdRetailServiceImpl implements BdRetailService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RetailMapper RetailMapper;
    @Autowired
    private BillMapper billMapper;

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
                throw new ServiceException(UtilConstants.ResponseMsg.NOT_RETAIL_CUSTOMER);
            }
            //查询零售记录,按时间顺序升序查看
            List<Retail> retailList = RetailMapper.selectList(new QueryWrapper<Retail>()
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

    @Override
    @Transactional
    public Message insertRetailDetails(SaveRetailDeatilsRequestDTO saveRetailDeatilsRequestDTO) throws ServiceException {
        /**
         * 1.插入一条零售记录
         * 2.账单实收金额要累加
         */
        Message message = new Message();
        try {
            Retail bdRetail = new Retail();
            TransferUtil.copyProperties(bdRetail, saveRetailDeatilsRequestDTO);
            //应收金额后台重新计算
            bdRetail.setAmount(bdRetail.getPrice().multiply(new BigDecimal(bdRetail.getQuantity())));
            int insertNum = RetailMapper.insert(bdRetail);
            if (insertNum < 0) {
                log.error("insertRetailDetails sql error");
                throw new ServiceException("insertRetailDetails sql error");
            }
            Bill bill = billMapper.selectOne(new QueryWrapper<Bill>().eq("id", saveRetailDeatilsRequestDTO.getBillId()));
            if (null != bill.getRealAmount()) {
                BigDecimal currentRealAmount = bill.getRealAmount();
                currentRealAmount = currentRealAmount.add(saveRetailDeatilsRequestDTO.getRealAmount());
                bill.setRealAmount(currentRealAmount);
            } else {
                bill.setRealAmount(saveRetailDeatilsRequestDTO.getRealAmount());
            }
            int updateNum = billMapper.updateById(bill);
            if (updateNum < 0) {
                log.error("Retail updateBill RealAmount sql not effect");
                throw new ServiceException("Retail updateBill RealAmount sql not effect");
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("insertRetailDetails service error:" + e);
            throw new ServiceException("insertRetailDetails service error");
        }
    }
}
