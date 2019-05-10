package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.model.BarCodeInfoDTO;
import com.zero.egg.model.Goods;
import com.zero.egg.responseDTO.GoodsResponse;
import com.zero.egg.service.IGoodsService;
import com.zero.egg.tool.JsonUtils;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-04-23
 */
@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Override
    public List<GoodsResponse> listByCondition(QueryWrapper<Goods> queryWrapper) {
        // TODO Auto-generated method stub
        return baseMapper.listByCondition(queryWrapper);
    }

    @Override
    @Transactional
    public Message querySingleGoodByBarCodeInfo(BarCodeInfoDTO infoDTO, String employeeId
            , String employeeName, String taskId, String customerId) {
        Message message = new Message();
        List<GoodsResponse> goodsResponseList;
        try {
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>()
                    .eq("g.company_id", infoDTO.getCompanyId())
                    .eq("g.shop_id", infoDTO.getShopId())
                    .eq("g.goods_no", infoDTO.getCurrentCode())
                    .eq("g.supplier_id", infoDTO.getSupplierId())
                    .eq("g.goods_category_id", infoDTO.getCategoryId())
                    .eq("g.dr", 0);
            GoodsResponse goods = baseMapper.queryGoodWhileShiping(queryWrapper);
            if (null == goods) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_SUCH_GOOD);
            }
            goods.setEmployeeId(employeeId);
            goods.setEmployeeName(employeeName);
            //存redis
            /**
             * 存redis
             * 1.从redis获取当前任务的列表json字符串
             * 2.把当前列表转化成List
             * 3.给list添加当前的扫的出货对象
             * 4.把List转换成json字符串,存redis
             */
            //第一次扫码出货时,redis没有相关任务键
            if (!jedisKeys.exists(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + infoDTO.getCompanyId() + infoDTO.getShopId() + customerId + taskId)
                    || null != jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + infoDTO.getCompanyId() + infoDTO.getShopId() + customerId + taskId)) {
                goodsResponseList = new ArrayList<>();
                goodsResponseList.add(goods);
                String goodsJson = JsonUtils.objectToJson(goodsResponseList);
                jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                        + infoDTO.getCompanyId() + infoDTO.getShopId() + customerId + taskId, goodsJson);
            } else {
                String goodsJson = jedisStrings.get(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                        + infoDTO.getCompanyId() + infoDTO.getShopId() + customerId + taskId);
                goodsResponseList = JsonUtils.jsonToList(goodsJson, GoodsResponse.class);
                goodsResponseList.add(goods);
                String newGoodsJson = JsonUtils.objectToJson(goodsResponseList);
                jedisStrings.set(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                        + infoDTO.getCompanyId() + infoDTO.getShopId() + customerId + taskId, newGoodsJson);
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
//            message.setData(goods);
        } catch (Exception e) {
            log.error("querySingleGoodByBarCodeInfo failed :" + e);
            throw new ServiceException("querySingleGoodByBarCodeInfo failed");
        }
        return message;
    }

}
