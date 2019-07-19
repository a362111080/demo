package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.BillDetailsMapper;
import com.zero.egg.dao.BillMapper;
import com.zero.egg.enums.BillEnums;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.BillRequest;
import com.zero.egg.requestDTO.BlankBillRequestDTO;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.responseDTO.BlankBillDTO;
import com.zero.egg.responseDTO.CategorySum;
import com.zero.egg.service.IBillService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
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
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {


    @Autowired
    private BillMapper mapper;

    @Autowired
    private BillDetailsMapper billDetailsMapper;

    @Override
    public List<Supplier> GetSupplierList(SupplierRequestDTO model) {
        return mapper.GetSupplierList(model);
    }

    @Override
    public List<Customer> GetCustomerList(CustomerRequestDTO model) {
        return mapper.GetCustomerList(model);

    }

    @Override
    public List<Bill> getBilllist(BillRequest model) {
        return mapper.getBilllist(model);
    }

    @Override
    public List<CategorySum> getBillCategorySum(String id) {
        return mapper.getBillCategorySum(id);
    }

    @Override
    @Transactional
    public Message updateBillAndDetails(BlankBillRequestDTO blankBillRequestDTO, LoginUser loginUser) {
        Message message = new Message();
        try {
            /*
             * 1.根据传入的账单status不为0,则不能修改
             * 2.新增账单细节,统计每个方案细节的应收金额和整个账单的应收金额
             * 3.更新账单信息 BillEnums.Status.Normal.toString()
             */
            //账单id
            String billId = blankBillRequestDTO.getBlankBillDTOList().get(0).getBillId();
            //实收金额
            BigDecimal realAmount = blankBillRequestDTO.getRealAmount();
            String currentsStutus = mapper.selectOne(new QueryWrapper<Bill>()
                    .select("status")
                    .eq("id", billId)
                    .eq("shop_id", loginUser.getShopId())
                    .eq("company_id", loginUser.getCompanyId())
                    .eq("dr", 0))
                    .getStatus();
            if (BillEnums.Status.Not_Generated.index().equals(currentsStutus)) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NOT_BLANK_BILL);
                return message;
            }

            String creator = loginUser.getId();
            String companyId = loginUser.getCompanyId();
            String shopId = loginUser.getShopId();

            //整个账单应收金额和数量
            BigDecimal amount = BigDecimal.ZERO;
            BigDecimal totalAuatity = BigDecimal.ZERO;
            List<BlankBillDTO> blankBillDTOList = blankBillRequestDTO.getBlankBillDTOList();
            BillDetails billDetails = null;
            for (BlankBillDTO blankBillDTO : blankBillDTOList) {
                String categoryId = blankBillDTO.getCategoryId();
                billDetails = new BillDetails();
                billDetails.setCompanyId(companyId);
                billDetails.setShopId(shopId);
                billDetails.setBillId(billId);
                billDetails.setGoodsCategoryId(categoryId);
                billDetails.setProgramId(blankBillDTO.getProgramId());
                billDetails.setGoodsCategoryId(categoryId);
                billDetails.setSpecificationId(blankBillDTO.getSpecificationId());
                //前端新输入的单价
                BigDecimal price = blankBillDTO.getPrice();
                BigDecimal quantity = new BigDecimal(blankBillDTO.getQuantity());
                /*
                 * 账单细节应收金额后端重新计算
                 * 如果货物原本是去皮方式,现在也是去皮方式,小计=价格*重量
                 * 如果货物原本是包方式,现在也是包方式,小计=价格*数量
                 * 如果货物原本是去皮方式,现在是包方式,小计=价格*数量
                 * 如果货物原本是包方式,现在是去皮方式,小计=价格*(重量-数量*去皮值),去皮值=blankBillDTO.getNumberical()
                 */
                BigDecimal subTotal;
                /* 1:去皮 2:包*/
                if (1 == blankBillDTO.getCurrentMode()) {
                    billDetails.setCurrentMode(1);
                    if (1 == blankBillDTO.getMode()) {
                        billDetails.setTotalWeight(blankBillDTO.getTotalWeight());
                        /* 去皮进,按斤出 */
                        subTotal = price.multiply(blankBillDTO.getTotalWeight());
                    } else if (2 == blankBillDTO.getMode()) {
                        billDetails.setTotalWeight(blankBillDTO.getTotalWeight());
                        billDetails.setNumberical(blankBillDTO.getNumberical());
                        /* 包进,按斤出 */
                        subTotal = price.multiply(blankBillDTO.getTotalWeight().subtract(quantity.multiply(blankBillDTO.getNumberical())));
                    } else {
                        log.error("param Exception,oriMode or currentMode is dissatisfied");
                        throw new ServiceException("param Exception,oriMode or currentMode is dissatisfied");
                    }
                } else if (2 == blankBillDTO.getCurrentMode()) {
                    billDetails.setCurrentMode(2);
                    billDetails.setTotalWeight(blankBillDTO.getTotalWeight());
                    /* 无论是包进,还是去皮进,如果按箱出,小计=单价*数量 */
                    subTotal = price.multiply(quantity);
                } else {
                    log.error("param Exception,oriMode or currentMode is dissatisfied");
                    throw new ServiceException("param Exception,oriMode or currentMode is dissatisfied");
                }
                billDetails.setPrice(price);
                billDetails.setQuantity(quantity);
                billDetails.setAmount(subTotal);
                billDetails.setCreator(creator);
                billDetails.setCreatetime(new Date());
                billDetails.setModifier(creator);
                billDetails.setModifytime(new Date());
                billDetails.setDr(false);
                billDetailsMapper.insert(billDetails);
                amount = amount.add(subTotal);
                totalAuatity = totalAuatity.add(quantity);
            }
            Bill bill = new Bill();
            bill.setId(billId);
            bill.setRealAmount(realAmount);
            bill.setAmount(amount);
            bill.setModifier(creator);
            bill.setModifytime(new Date());
            bill.setQuantity(totalAuatity);
            bill.setStatus(BillEnums.Status.Normal.index().toString());
            mapper.updateById(bill);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("updateBillAndDetails failed:" + e);
            throw new ServiceException("updateBillAndDetails failed");
        }
    }

    @Override
    @Transactional
    public Message updateBillAndDetails(Bill bill, LoginUser loginUser) throws ServiceException {
        Message message = new Message();
        try {
            BigDecimal amount = BigDecimal.ZERO;
            //账单细节列表
            List<BillDetails> details = bill.getUnloadDetails();
            //需要更新库的账单细节对象
            BillDetails IDetails = new BillDetails();
            BigDecimal subTotal;
            BigDecimal numberical;
            for (BillDetails billDetails : details) {
                IDetails.setPrice(billDetails.getPrice());
                //如果当前计重方式为去皮,小计=单价*(重量-去皮值*数量)
                if (1 == billDetails.getCurrentMode()) {
                    //去皮值可能为null
                    if (null == billDetails.getNumberical() || billDetails.getNumberical().compareTo(BigDecimal.ZERO) == 0) {
                        numberical = BigDecimal.ZERO;
                    } else {
                        numberical = billDetails.getNumberical();
                    }
                    subTotal = billDetails.getPrice()
                            .multiply(billDetails.getTotalWeight()
                                    .subtract(billDetails.getQuantity()
                                            .multiply(numberical)));
                } else if (2 == billDetails.getCurrentMode()) {
                    subTotal = billDetails.getPrice().multiply(billDetails.getQuantity());
                } else {
                    //如果缺少参数就返回前端并提示
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
                    return message;
                }
                amount.add(subTotal);
                IDetails.setAmount(subTotal);
                billDetailsMapper.updateDetails(IDetails);
            }
            //更新总价
            bill.setAmount(amount);
            bill.setModifier(loginUser.getId());
            bill.setModifytime(new Date());
            mapper.updateById(bill);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("updateBillAndDetails error" + e);
            throw new ServiceException("updateBillAndDetails error");
        }
        return message;
    }
}
