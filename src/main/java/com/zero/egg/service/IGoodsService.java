package com.zero.egg.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.Goods;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.ShipmentGoodBarCodeRequestDTO;
import com.zero.egg.responseDTO.GoodsResponse;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsResponse> listByCondition(QueryWrapper<Goods> queryWrapper);

    /**
     * 根据二维码信息查询商品信息(出货用)
     *
     * @return
     * @throws ServiceException
     */
    Message querySingleGoodByBarCodeInfo(ShipmentGoodBarCodeRequestDTO shipmentGoodRequestDTO, LoginUser loginUser) throws ServiceException;
}
