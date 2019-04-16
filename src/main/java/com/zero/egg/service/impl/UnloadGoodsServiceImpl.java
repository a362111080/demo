package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.UnloadGoodsMapper;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.responseDTO.UnLoadGoodsQueryResponseDto;
import com.zero.egg.responseDTO.UnLoadResponseDto;
import com.zero.egg.service.IUnloadGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
@Service
public class UnloadGoodsServiceImpl extends ServiceImpl<UnloadGoodsMapper, UnloadGoods> implements IUnloadGoodsService {

    @Autowired
    private UnloadGoodsMapper mapper;

    @Override
    public int AddUnloadDetl(UnloadGoods model) {
        return mapper.AddUnloadDetl(model);
    }

    @Override
    public UnLoadResponseDto CheckWeight(BigDecimal weight, String programId) {
        return  mapper.CheckWeight(weight,programId);
    }

    @Override
    public List<UnLoadGoodsQueryResponseDto> QueryUnloadGood(String taskId) {
        return  mapper.QueryUnloadGood(taskId);
    }

    @Override
    public List<UnLoadGoodsQueryResponseDto> QueryUnloadGoodForDay(String shopId, String unloadTime) {
        return  mapper.QueryUnloadGoodForDay(shopId,unloadTime);
    }
}
