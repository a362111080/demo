package com.zero.egg.dao;

import com.zero.egg.model.Bill;
import com.zero.egg.model.BillDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-14
 */
public interface BillDetailsMapper extends BaseMapper<BillDetails> {

    List<BillDetails> getbilldetsils(Bill bill);

    Boolean updateDetails(BillDetails iDetails);

}
