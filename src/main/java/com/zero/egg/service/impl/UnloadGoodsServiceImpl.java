package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.UnloadGoodsMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.Task;
import com.zero.egg.model.UnloadGoods;
import com.zero.egg.requestDTO.UnloadGoodsRequest;
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

    @Override
    public int GetTaskUnloadCount(String taskId) {
        return  mapper.GetTaskUnloadCount(taskId);
    }

    @Override
    public List<UnloadGoods> GetUnloadList(UnloadGoodsRequest unloadGoods) {

        return  mapper.GetUnloadList(unloadGoods);
    }

    @Override
    public String GetTaskStatusBySupplier(String supplierId) {
        return  mapper.GetTaskStatusBySupplier(supplierId);
    }

    @Override
    public UnLoadGoodsQueryResponseDto GetTaskProgram(String taskId) {
        return  mapper.GetTaskProgram(taskId);
    }

    @Override
    public BarCode GetBarCodeInfo(String qrCode) {

        return  mapper.GetBarCodeInfo(qrCode);
    }

    @Override
    public UnLoadResponseDto CheckWeightForWarning( String programId) {
        return  mapper.CheckWeightForWarning(programId);
    }

    @Override
    public int GoodNoIsExists(String currentCode) {
        return  mapper.GoodNoIsExists(currentCode);
    }

    @Override
    public void RepaireUnloadTask(String taskId) {
          mapper.RepaireUnloadTask(taskId);
    }

    @Override
    public int RemoveUnloadGood(UnloadGoods model) {

        Task  unload =mapper.GetTaskinfo(model.getTaskId());
        if (null!=unload) {
            return mapper.RemoveUnloadGood(model);
        }
        else
        {
            return -99;
        }

    }

    @Override
    public List<UnLoadGoodsQueryResponseDto> QueryUnloadGoodForTimeSpan(UnloadGoods model) {
        return  mapper.QueryUnloadGoodForTimeSpan(model);
    }
}
