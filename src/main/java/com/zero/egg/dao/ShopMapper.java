package com.zero.egg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.egg.model.*;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.requestDTO.ShopRequest;
import com.zero.egg.responseDTO.OrderCategoryResponseDTO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
public interface ShopMapper extends BaseMapper<Shop> {

    List<Shop> getShopListByCompanid(String companyId);

    Integer getClietnUseCountByShopid(String shopId, Integer type);

    int addsecret(OrderSecret se);

    List<OrderSecret> GetShopSecret(ShopRequest shop);

    List<OrderSecret> GetOrderCategoryList(String pid, String level);

    int addordercategory(OrderCategory model);

    int editrdercategory(OrderCategory model);

    List<OrderCategoryResponseDTO> GetOrderCateGory(OrderCategory model);

    List<OrderCategory> GetOrderCateGoryChild(OrderCategory model);

    int addordergood(OrderGoods model);

    int GetOrderGoodsSort(OrderGoods model);

    int editordergood(OrderGoods model);

    List<OrderGoods> GetOrderGoods(OrderGoodsRequestDTO model);

    int editsecret(OrderSecret model);

    int addordergoodspec(OrderGoodSpecification ogs);

    int editordergoodspec(OrderGoodSpecification ogs);

    List<OrderGoodSpecification> GetOrderGoodSpecList(OrderGoods orderGoods);

    OrderGoods getOrderGoodsForCheck(OrderGoods model);

    int updateGoodSepcification(OrderGoods model);

    List<OrderCategory> getCategoryInfo(OrderCategory model);
}
