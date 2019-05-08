package com.zero.egg.service;

import com.zero.egg.model.Bill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.Supplier;
import com.zero.egg.requestDTO.SupplierRequestDTO;

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
}
