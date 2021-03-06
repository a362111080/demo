package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.BarCodeMapper;
import com.zero.egg.dao.CustomerMapper;
import com.zero.egg.dao.GoodsMapper;
import com.zero.egg.dao.ShipmentGoodsMapper;
import com.zero.egg.dao.TaskMapper;
import com.zero.egg.model.BarCode;
import com.zero.egg.model.Customer;
import com.zero.egg.model.Goods;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.model.Task;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ShipmentGoodsMapper shipmentGoodsMapper;

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
                    .eq("g.company_id", loginUser.getCompanyId())
                    .eq("g.shop_id", loginUser.getShopId())
                    .eq("g.goods_no", barCode.getCurrentCode())
                    .eq("g.supplier_id", barCode.getSupplierId())
                    .eq("g.goods_category_id", barCode.getCategoryId())
                    .eq("g.dr", 0);
            GoodsResponse goods = baseMapper.queryGoodWhileShiping(queryWrapper);
            if (null == goods) {
                throw new ServiceException(UtilConstants.ResponseMsg.NO_SUCH_GOOD);
            }
            Integer shipmentFlag = shipmentGoodsMapper.selectCount(new QueryWrapper<ShipmentGoods>()
                    .eq("company_id", loginUser.getCompanyId())
                    .eq("shop_id", loginUser.getShopId())
                    .eq("goods_no", barCode.getCurrentCode())
                    .eq("dr", 0));
            if (shipmentFlag > 0) {
                throw new ServiceException(UtilConstants.ResponseMsg.IN_OTHER_TASK);
            }
            //如果重量为0，则表示之前是不过称卸货，那出货一定要称重
            if (goods.getWeight().compareTo(BigDecimal.ZERO)==0) {
                if (null==shipmentGoodRequestDTO.getWeight()
                        ||shipmentGoodRequestDTO.getWeight().compareTo(BigDecimal.ZERO)<0) {
                    throw new ServiceException("未称重货物，请接连蓝牙称称重后出货");
                }
            }
            Task temptask = taskMapper.selectOne(new QueryWrapper<Task>()
                    .select("is_weight")
                    .eq("company_id", loginUser.getCompanyId())
                    .eq("shop_id", loginUser.getShopId())
                    .eq("id", taskId)
                    .eq("dr", 0)
            );
            if (null == temptask || null == temptask.getIsWeight()) {
                throw new ServiceException("出货任务不存在或任务属性出错，请取消后重新创建");
            }
            //如果任务类型是需要称重的，则也需要重新称重
            if (1==temptask.getIsWeight()) {
                if (null==shipmentGoodRequestDTO.getWeight()
                        ||shipmentGoodRequestDTO.getWeight().compareTo(BigDecimal.ZERO)<0) {
                    throw new ServiceException("称重出货任务，请接连蓝牙称称重后出货，原重量为："+goods.getWeight());
                }
                goods.setWeight(shipmentGoodRequestDTO.getWeight());
            }
            goods.setEmployeeId(employeeId);
            goods.setEmployeeName(employeeName);
/*            //如果合作商是零售,查询当前sortedSet里面的数量,限制出货数量为1
            Customer customer = customerMapper.getOneById(customerId);
            if (1 == customer.getIsRetail()) {
                long shipedNum = sortSets.zcard(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                        + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId);
                if (shipedNum >= 1) {
                    throw new ServiceException(UtilConstants.ResponseMsg.RETAIL_ONE_ONLY);
                }
            }*/
            // 存redis
            long effectiveNum = sortSets.zaddNx(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                            + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId
                    , (double) System.currentTimeMillis(), JsonUtils.objectToJson(goods));
            //要判断商品不能重复被添加
            if (effectiveNum < 1) {
                throw new ServiceException((UtilConstants.ResponseMsg.DUPLACTED_DATA));
            }
            log.info("redis key:" + UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId
            );
            ShipmentGoods shipmentGoods = new ShipmentGoods();
            shipmentGoods.setCompanyId(goods.getCompanyId());
            shipmentGoods.setShopId(goods.getShopId());
            shipmentGoods.setCustomerId(customerId);
            shipmentGoods.setTaskId(taskId);
            shipmentGoods.setSpecificationId(goods.getSpecificationId());
            shipmentGoods.setGoodsCategoryId(goods.getGoodsCategoryId());
            shipmentGoods.setGoodsNo(goods.getGoodsNo());
            shipmentGoods.setMode(goods.getMode());
            shipmentGoods.setMarker(goods.getMarker());
            shipmentGoods.setWeight(goods.getWeight());
            shipmentGoods.setRemark(goods.getRemark());
            shipmentGoods.setCreatetime(new Date());
            shipmentGoods.setModifytime(new Date());
            shipmentGoods.setModifier(loginUser.getId());
            shipmentGoods.setCreator(loginUser.getId());
            shipmentGoods.setDr(false);
            shipmentGoodsMapper.insert(shipmentGoods);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } catch (ServiceException e) {
            throw e;
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
             * 获取当前出货任务需要删除的出货商品集合
             */
            List<GoodsResponse> goodsResponseList = remShipmentGoodsRequestDTO.getGoodsResponseList();
            Set<String> shipmentGoodsSet;
            ShipmentGoods shipmentGoods = new ShipmentGoods();
            //移除出货任务中的货物代表出货商品表对应货物作废
            shipmentGoods.setDr(true);
            for (GoodsResponse goodsResponse : goodsResponseList) {
                //实际只有一个元素,取出
                shipmentGoodsSet = sortSets.zrangeByScore(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                                + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":"
                                + remShipmentGoodsRequestDTO.getCustomerId() + ":" + remShipmentGoodsRequestDTO.getTaskId()
                        , goodsResponse.getScores(), goodsResponse.getScores());
                String jsonString = shipmentGoodsSet.iterator().next();
                GoodsResponse redisGood = JsonUtils.jsonToPojo(jsonString, GoodsResponse.class);
                int updateNum = shipmentGoodsMapper.update(shipmentGoods, new UpdateWrapper<ShipmentGoods>()
                        .eq("goods_no", redisGood.getGoodsNo())
                        .eq("task_id", remShipmentGoodsRequestDTO.getTaskId())
                        .eq("customer_id", remShipmentGoodsRequestDTO.getCustomerId())
                        .eq("dr", 0));
                if (updateNum < 1) {
                    throw new ServiceException("removeShipmentGoods from sql failed");
                }
                sortSets.zremrangeByScore(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                                + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":"
                                + remShipmentGoodsRequestDTO.getCustomerId() + ":" + remShipmentGoodsRequestDTO.getTaskId()
                        , goodsResponse.getScores(), goodsResponse.getScores());
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
