package com.zero.egg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.Bill;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.BillRequest;
import com.zero.egg.requestDTO.BlankBillRequestDTO;
import com.zero.egg.requestDTO.CustomerRequestDTO;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.SupplierRequestDTO;
import com.zero.egg.responseDTO.CategorySum;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
public interface IBillService extends IService<Bill> {

    List<Supplier> GetSupplierList(SupplierRequestDTO model);

    List<Customer> GetCustomerList(CustomerRequestDTO model);

    List<Bill> getBilllist(BillRequest model);

    List<CategorySum> getBillCategorySum(String id);

    /**
     * 更新空的出货账单信息
     *
     * @param blankBillRequestDTO
     * @return
     */
    void updateBillAndDetails(BlankBillRequestDTO blankBillRequestDTO, LoginUser loginUser);
}
