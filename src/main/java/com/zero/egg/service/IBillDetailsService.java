package com.zero.egg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.zero.egg.tool.Message;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
public interface IBillDetailsService extends IService<BillDetails> {

    Message getbilldetsils(Bill bill);

    Boolean updateDetails(BillDetails iDetails);
}
