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
            /**
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
                //细节应收金额后端重新计算
                //如果是去皮的方式,则按重量算价格
                BigDecimal subTotal;
                if (1 == blankBillDTO.getMode()) {
                    subTotal = price.multiply(blankBillDTO.getTotalWeight());
                } else {
                    subTotal = price.multiply(quantity);
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
            bill.setStatus(BillEnums.Status.Normal.toString());
            mapper.updateById(bill);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.error("updateBillAndDetails failed:" + e);
            throw new ServiceException("updateBillAndDetails failed");
        }
    }
}
