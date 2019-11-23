package com.zero.egg.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.Bill;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.BillRequest;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.responseDTO.CategorySum;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
public interface BillMapper extends BaseMapper<Bill> {

    List<Supplier> GetSupplierList(SupplierRequestDTO model);

    List<Customer> GetCustomerList(CustomerRequestDTO model);

    List<Bill> getBilllist(BillRequest model);

    List<CategorySum> getBillCategorySum(String id);

    /**
     * 根据主键id,店铺id,企业id查询账单,且账单未被删除
     * @param bill
     * @return
     */
    Bill getOneByIdAndCompanyIdAndShopIdAndDr(Bill bill);

}
