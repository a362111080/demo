package com.zero.egg.service;

import com.zero.egg.model.UnloadGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.responseDTO.UnLoadResponseDto;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
public interface IUnloadGoodsService extends IService<UnloadGoods> {

    int AddUnloadDetl(UnloadGoods model);

    UnLoadResponseDto CheckWeight(BigDecimal weight, String programId);
}
