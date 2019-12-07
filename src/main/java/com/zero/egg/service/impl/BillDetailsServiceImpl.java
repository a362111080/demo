package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.BillDetailsMapper;
import com.zero.egg.dao.BillMapper;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.responseDTO.BillDetailsResponseDTO;
import com.zero.egg.service.IBillDetailsService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
@Service
@Slf4j
public class BillDetailsServiceImpl extends ServiceImpl<BillDetailsMapper, BillDetails> implements IBillDetailsService {

    @Autowired
    private BillDetailsMapper mapper;

    @Autowired
    private BillMapper billMapper;

    @Override
    public Message getbilldetsils(Bill bill) {
        Message message = new Message();
        BillDetailsResponseDTO billDetailsResponseDTO = new BillDetailsResponseDTO();
        try {
            Bill actualBill = billMapper.getOneByIdAndCompanyIdAndShopIdAndDr(bill);
            //如果没查出该账单信息则返回提示信息
            if (null != actualBill) {
                billDetailsResponseDTO.setAmount(actualBill.getAmount());
                billDetailsResponseDTO.setRealAmount(actualBill.getRealAmount());
                List<BillDetails> billDetailsList = mapper.getbilldetsils(bill);
                billDetailsResponseDTO.setBillDetailsList(billDetailsList);
                billDetailsResponseDTO.setBillId(actualBill.getId());
                billDetailsResponseDTO.setBillNo(actualBill.getBillNo());
                billDetailsResponseDTO.setOrderSn(actualBill.getOrderSn());
                message.setData(billDetailsResponseDTO);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.PARAM_ERROR);
            }
        } catch (Exception e) {
            log.error("getbilldetsils error :" + e);
            throw new ServiceException("getbilldetsils error");
        }
        return message;
    }

    @Override
    public Boolean updateDetails(BillDetails iDetails) {

        return mapper.updateDetails(iDetails);
    }
}
