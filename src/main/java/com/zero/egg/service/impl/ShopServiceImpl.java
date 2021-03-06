package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.egg.dao.OrderCartMapper;
import com.zero.egg.dao.OrderGoodsSpecificationMapper;
import com.zero.egg.dao.OrderUserSecretMapper;
import com.zero.egg.dao.ShopMapper;
import com.zero.egg.enums.CompanyUserEnums;
import com.zero.egg.model.*;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.requestDTO.ShopRequest;
import com.zero.egg.responseDTO.OrderCategoryResponseDTO;
import com.zero.egg.service.IShopService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import com.zero.egg.tool.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
@Service
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Autowired
    private OrderCartMapper orderCartMapper;

    @Autowired
    private OrderGoodsSpecificationMapper orderGoodsSpecificationMapper;

    @Autowired
    private OrderUserSecretMapper orderUserSecretMapper;

    @Override
    public boolean save(Shop shop) {
        shop.setId(UuidUtil.get32UUID());
        shop.setCreatetime(new Date());
        shop.setModifytime(new Date());
        shop.setStatus(CompanyUserEnums.Status.Normal.index().toString());
        shop.setDr(false);
        return super.save(shop);
    }

    @Override
    public Message<Object> save(Shop shop, LoginUser loginUser) {
        Message<Object> message = new Message<>();
        shop.setId(UuidUtil.get32UUID());
        shop.setCreatetime(new Date());
        shop.setModifytime(new Date());
        shop.setStatus(CompanyUserEnums.Status.Normal.index().toString());
        shop.setDr(false);
        shop.setModifier(loginUser.getId());
        shop.setCreator(loginUser.getId());
        if (save(shop)) {
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        } else {
            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.FAILED);
        }
        return null;

    }


    @Autowired
    private ShopMapper mapper;

    @Override
    public List<Shop> getShopListByCompanid(String companyId) {
        return mapper.getShopListByCompanid(companyId);
    }

    @Override
    public Integer getClietnUseCountByShopid(String shopId, Integer type) {
        return mapper.getClietnUseCountByShopid(shopId, type);
    }

    @Override
    public int addsecret(Shop shop, LoginUser loginUser, String usecret) {
        OrderSecret se = new OrderSecret();
        se.setCompanyid(loginUser.getCompanyId());
        se.setShopid(loginUser.getShopId());
        se.setSecretKey(usecret);
        se.setCustomerId(shop.getCustomerId());
        se.setCreator(loginUser.getId());
        se.setModifier(loginUser.getId());
        se.setStatus(false);
        se.setCreatetime(new Date());
        se.setModifytime(new Date());
        se.setDr(false);
        return mapper.addsecret(se);
    }

    @Override
    public List<OrderSecret> GetShopSecret(ShopRequest shop) {
        return mapper.GetShopSecret(shop);
    }

    @Override
    public int addordercategory(OrderCategory model, LoginUser loginUser) {
        model.setId(UuidUtil.get32UUID());
        model.setModifytime(new Date());
        model.setCreatetime(new Date());
        model.setShopId(loginUser.getShopId());
        model.setCompanyId(loginUser.getCompanyId());
        model.setDr(false);
        return mapper.addordercategory(model);
    }

    @Override
    public int editrdercategory(OrderCategory model) {
        return mapper.editrdercategory(model);
    }

    @Override
    public List<OrderCategoryResponseDTO> GetOrderCateGory(OrderCategory model) {
        return mapper.GetOrderCateGory(model);
    }

    @Override
    public List<OrderCategory> GetOrderCateGoryChild(OrderCategory model) {
        return mapper.GetOrderCateGoryChild(model);
    }

    @Override
    public int addordergood(OrderGoods model, LoginUser loginUser) {
        model.setId(UuidUtil.get32UUID());
        model.setShopId(loginUser.getShopId());
        model.setCompanyId(loginUser.getCompanyId());
        model.setModifytime(new Date());
        model.setCreatetime(new Date());
        model.setDr(false);
        return mapper.addordergood(model);
    }

    @Override
    public int GetOrderGoodsSort(OrderGoods model) {
        return mapper.GetOrderGoodsSort(model);
    }

    @Override
    @Transactional
    public int editordergood(OrderGoods model, LoginUser loginUser) {
        model.setShopId(loginUser.getShopId());
        model.setCompanyId(loginUser.getCompanyId());
        model.setModifytime(new Date());
        model.setCreatetime(new Date());
        model.setDr(model.getDr());
        try {
            //如果下架或者删除商品,则商品下所有规格也删除,购物车对应商品也作废
            if (model.getDr() || model.getIsOnSale() == false) {

                orderGoodsSpecificationMapper.update(new OrderGoodsSpecification().setDr(true)
                        , new UpdateWrapper<OrderGoodsSpecification>()
                                .eq("goods_id", model.getId()));
                orderCartMapper.update(new OrderCart().setEffectFlag(false), new UpdateWrapper<OrderCart>()
                        .eq("shop_id", model.getShopId())
                        .eq("goods_id", model.getId()));
            }
            return mapper.editordergood(model);
        } catch (Exception e) {
            log.error("删除商品失败" + e);
            throw new ServiceException("删除商品失败" + e);
        }
    }

    @Override
    public List<OrderGoods> GetOrderGoods(OrderGoodsRequestDTO model) {
        return mapper.GetOrderGoods(model);
    }

    @Override
    @Transactional
    public int editsecret(OrderSecret model) {
        try {
            //也要删除关联表数据,因为没有删除秘钥的功能,所以不用删除
//            orderUserSecretMapper.update(new OrderUserSecret().setDr(true), new UpdateWrapper<OrderUserSecret>()
//                    .eq("secret_id", model.getId()));
            return mapper.editsecret(model);
        } catch (Exception e) {
            log.error("编辑秘钥信息失败" + e);
            throw new ServiceException("编辑秘钥信息失败" + e);
        }
    }

    @Override
    public int addordergoodspec(OrderGoodSpecification ogs) {
        return mapper.addordergoodspec(ogs);
    }

    @Override
    public int editordergoodspec(OrderGoodSpecification ogs) {
        return mapper.editordergoodspec(ogs);
    }

    @Override
    public List<OrderGoodSpecification> GetOrderGoodSpecList(OrderGoods orderGoods) {
        return mapper.GetOrderGoodSpecList(orderGoods);
    }

    @Override
    public OrderGoods getOrderGoodsForCheck(OrderGoods model) {

        return mapper.getOrderGoodsForCheck(model);
    }

    @Override
    public int updateGoodSepcification(OrderGoods model) {
        return mapper.updateGoodSepcification(model);
    }

    @Override
    public List<OrderCategory> getCategoryInfo(OrderCategory model) {

        return mapper.getCategoryInfo(model);
    }

    @Override
    public List<User> getUserList(User user) {
        return mapper.getUserList(user);
    }

    @Override
    public List<OrderGoods> GetOrderGoodsList(OrderCategory model) {
        return mapper.GetOrderGoodsList(model);
    }

    @Override
    public List<OrderCategory> GetOrderCateGoryList(OrderGoodsRequestDTO model) {
        return mapper.GetOrderCateGoryList(model);
    }

}
