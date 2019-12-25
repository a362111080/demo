package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.cache.JedisUtil;
import com.zero.egg.dao.ShipmentGoodsMapper;
import com.zero.egg.dao.SpecificationMapper;
import com.zero.egg.model.ShipmentGoods;
import com.zero.egg.model.Specification;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.ShipmentGoodsRequest;
import com.zero.egg.requestDTO.ShipmentStaticRequestDTO;
import com.zero.egg.responseDTO.GoodsResponse;
import com.zero.egg.responseDTO.ShipmentGoodsResponse;
import com.zero.egg.service.IShipmentGoodsService;
import com.zero.egg.tool.JsonUtils;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.PageDTO;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ShipmentGoodsServiceImpl extends ServiceImpl<ShipmentGoodsMapper, ShipmentGoods> implements IShipmentGoodsService {

    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private JedisUtil.SortSets sortSets;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Override
    public IPage<ShipmentGoodsResponse> listByCondition(IPage<ShipmentGoods> page, QueryWrapper<ShipmentGoods> queryWrapper) {
        return super.baseMapper.listByCondition(page, queryWrapper);
    }

    @Override
    public List<ShipmentGoodsResponse> listByCondition(QueryWrapper<ShipmentGoods> queryWrapper) {
        return super.baseMapper.listByCondition(queryWrapper);
    }

    @Override
    public List<ShipmentGoodsResponse> countProgram(QueryWrapper<ShipmentGoods> queryWrapper) {
        return super.baseMapper.countprogram(queryWrapper);
    }

    @Override
    public List<ShipmentGoodsResponse> countCategory(QueryWrapper<ShipmentGoods> queryWrapper) {
        return super.baseMapper.countcategory(queryWrapper);
    }

    @Override
    public List<ShipmentGoodsResponse> countSpecification(QueryWrapper<ShipmentGoods> queryWrapper) {
        return super.baseMapper.countspecification(queryWrapper);
    }

    @Override
    public List<ShipmentGoodsResponse> todaycountcategory(ShipmentStaticRequestDTO shipmentStaticRequestDTO, QueryWrapper<ShipmentGoods> queryWrapper) {
        return super.baseMapper.todaycountcategory(shipmentStaticRequestDTO,queryWrapper);
    }

    @Override
    public List<ShipmentGoodsResponse> todaycountspecification(ShipmentStaticRequestDTO shipmentStaticRequestDTO,QueryWrapper<ShipmentGoods> queryWrapper) {
        return super.baseMapper.todaycountspecification(shipmentStaticRequestDTO,queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message<Object> listBySortType(LoginUser loginUser, ShipmentGoodsRequest shipmentGoodsRequest) {
        Message message = new Message();
        try {
            String customerId = shipmentGoodsRequest.getCustomerId();
            String taskId = shipmentGoodsRequest.getTaskId();
            /**
             * 如果redis里存在相关出货任务key  或者  存在kry,但是value为null,就返回查询成功,但是内容为null
             * 否则,直接从redis里面取出对应value,并转化为list集合存进Data里返回
             */
            if (!jedisKeys.exists(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                    + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId)
            ) {
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            } else {
                switch (shipmentGoodsRequest.getSortType()) {
                    //列表
                    case 1:
                        /**
                         * 分页信息
                         */
                        Long current = shipmentGoodsRequest.getCurrent();
                        Long size = shipmentGoodsRequest.getSize();
                        Long total = sortSets.zcard(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                                + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId);
                        Long pages;
                        if (total % size == 0) {
                            pages = total / size;
                        } else {
                            pages = total / size + 1;
                        }
                        Set<Tuple> tuples = sortSets.zrevrangeWithScores(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                                        + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId
                                , size.intValue() * (current.intValue() - 1), size.intValue() * current.intValue() - 1);
                        List<GoodsResponse> goodsResponseList = new ArrayList<>();
                        GoodsResponse redisGood;
                        for (Tuple tuple : tuples) {
                            redisGood = JsonUtils.jsonToPojo(tuple.getElement(), GoodsResponse.class);
                            redisGood.setScores(tuple.getScore());
                            goodsResponseList.add(redisGood);
                        }
                        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                        PageDTO pageDTO = new PageDTO();
                        pageDTO.setCurrent(current);
                        pageDTO.setSize(size);
                        pageDTO.setTotal(total);
                        pageDTO.setPages(pages);
                        message.setData(goodsResponseList);
                        message.setTotaldata(pageDTO);
                        break;
                    //整理归类
                    case 2:
                        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
                        Map<String, List<GoodsResponse>> categoryResultMap = new HashMap();
                        Set<String> goodsSetFor2 = sortSets.zrevrange(UtilConstants.RedisPrefix.SHIPMENTGOOD_TASK
                                        + loginUser.getCompanyId() + ":" + loginUser.getShopId() + ":" + customerId + ":" + taskId
                                , 0, -1);
                        List<GoodsResponse> goodsResponseList2 = new ArrayList<>();
                        GoodsResponse redisGoodTemp;
                        for (String jsonString : goodsSetFor2) {
                            redisGoodTemp = JsonUtils.jsonToPojo(jsonString, GoodsResponse.class);
                            goodsResponseList2.add(redisGoodTemp);
                        }
                        //按照品种名分类
                        for (GoodsResponse goodsResponse : goodsResponseList2) {
                            String categoryName = goodsResponse.getCategoryName();
                            if (categoryResultMap.keySet().contains(categoryName)) {
                                categoryResultMap.get(categoryName).add(goodsResponse);
                            } else {
                                List<GoodsResponse> tempList = new ArrayList<>();
                                tempList.add(goodsResponse);
                                categoryResultMap.put(categoryName, tempList);
                            }
                        }
                        //再按照方案区间分类
                        //根据specificationId查询所属规格specification
                        //mode=1 key拼接"'实重('+specification.getWeightmin()+'~'+specification.getWeightmax()+')'"
                        //mode=2 key=temp.getMarker()
                        Specification specification;
                        Integer count;
                        for (Map.Entry<String, List<GoodsResponse>> entry : categoryResultMap.entrySet()) {
                            Map<String, Integer> map = new HashMap();
                            for (GoodsResponse temp : entry.getValue()) {
                                specification = specificationMapper.selectOne(new QueryWrapper<Specification>()
                                        .select("weight_min", "weight_max", "mode")
                                        .eq("id", temp.getSpecificationId())
                                        .eq("shop_id", temp.getShopId())
                                        .eq("company_id", temp.getCompanyId()));
                                if (1==specification.getMode()) {
                                    count = map.get("实重("+specification.getWeightMin()+"~"+specification.getWeightMax()+")");
                                    map.put("实重("+specification.getWeightMin()+"~"+specification.getWeightMax()+")"
                                            , (count == null) ? 1 : count + 1);
                                } else if (2 == specification.getMode()) {
                                    count = map.get(temp.getMarker());
                                    map.put(temp.getMarker(), (count == null) ? 1 : count + 1);
                                }
                            }
                            map.put("total", entry.getValue().size());
                            resultMap.put(entry.getKey(), map);
                        }
                        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                        message.setMap(resultMap);
                        break;
                    default:
                        message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                        message.setMessage(UtilConstants.ResponseMsg.PARAM_MISSING);
                }
            }
        } catch (Exception e) {
            log.error("listBySortType failed:" + e);
            throw new ServiceException("listBySortType failed");
        }
        return message;
    }

}
