package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.BarCodeMapper;
import com.zero.egg.dao.CustomerMapper;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Goods;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.RemShipmentGoodsRequestDTO;
import com.zero.egg.requestDTO.ShipmentGoodBarCodeRequestDTO;
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
    private BarCodeMapper barCodeMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private JedisUtil.SortSets sortSets;

    @Override
    public List<GoodsResponse> listByCondition(QueryWrapper<Goods> queryWrapper) {
        // TODO Auto-generated method stub
        return baseMapper.listByCondition(queryWrapper);
    }

    @Override
    @Transactional
    public Message querySingleGoodByBarCodeInfo(ShipmentGoodBarCodeRequestDTO shipmentGoodRequestDTO, LoginUser loginUser) {
        Message message = new Message();
        try {
            /**
             * 从入参中获取二维码信息(二维码主键id)
             */
            String taskId = shipmentGoodRequestDTO.getTaskId();
            String customerId = shipmentGoodRequestDTO.getCustomerId();
            String barCodeId = shipmentGoodRequestDTO.getBarCodeString();
            String employeeId = loginUser.getId();
            String employeeName = loginUser.getName();

            BarCode barCode = barCodeMapper.selectById(barCodeId);
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>()
                    .eq("g.company_id", barCode.getCompanyId())
                    .eq("g.shop_id", barCode.getShopId())
                    .eq("g.goods_no", barCode.getCurrentCode())
                    .eq("g.supplier_id", barCode.getSupplierId())
                    .eq("g.goods_category_id", barCode.getCategoryId())
                    .eq("g.dr", 0);
            GoodsResponse goods = baseMapper.queryGoodWhileShiping(queryWrapper);
            if (null == goods) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.NO_SUCH_GOOD);
                return message;
            }
            goods.setEmployeeId(employeeId);
            goods.setEmployeeName(employeeName);
            //如果合作商是零售,查询当前sortedSet里面的数量,限制出货数量为1
            Customer customer = customerMapper.selectById(customerId);
            if (1 == customer.getIsRetail()) {
                long shipedNum = sortSets.zcard(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                        + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId);
                if (shipedNum >= 1) {
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage(UtilConstants.ResponseMsg.RETAIL_ONE_ONLY);
                    return message;
                }
            }
            /**
             * 存redis
             */
            long effectiveNum = sortSets.zaddNx(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                            + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId
                    , (double) System.currentTimeMillis(), JsonUtils.objectToJson(goods));

            /**
             * 要判断商品不能重复被添加
             */
            if (effectiveNum < 1) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
                return message;
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("querySingleGoodByBarCodeInfo failed :" + e);
            throw new ServiceException("querySingleGoodByBarCodeInfo failed");
        }
        return message;
    }

    @Override
    public Message removeShipmentGoods(RemShipmentGoodsRequestDTO remShipmentGoodsRequestDTO, LoginUser loginUser) {
        Message message = new Message();
        try {
            /**
             * 1. 获取当前出货任务需要删除的出货商品集合
             */
            List<GoodsResponse> goodsResponseList = remShipmentGoodsRequestDTO.getGoodsResponseList();
            for (GoodsResponse goodsResponse : goodsResponseList) {
                sortSets.zrem(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                                + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":"
                                + remShipmentGoodsRequestDTO.getCustomerId() + ":" + remShipmentGoodsRequestDTO.getTaskId()
                        , JsonUtils.objectToJson(goodsResponse));
            }
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (Exception e) {
            log.error("removeShipmentGoods failed :" + e);
            throw new ServiceException("removeShipmentGoods failed");
        }
        return message;
    }

}
