package com.zero.egg.service;

import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
public interface IBillDetailsService extends IService<BillDetails> {

    List<BillDetails> getbilldetsils(Bill bill);

}
