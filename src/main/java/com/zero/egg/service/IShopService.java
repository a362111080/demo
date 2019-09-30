package com.zero.egg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.model.*;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.requestDTO.ShopRequest;
import com.zero.egg.responseDTO.OrderCategoryResponseDTO;
import com.zero.egg.tool.Message;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
public interface IShopService extends IService<Shop> {
	
	Message<Object> save(Shop entity ,LoginUser loginUser) ;

    List<Shop> getShopListByCompanid(String companyId);

    Integer getClietnUseCountByShopid(String shopId, Integer type);

    int addsecret(Shop shop, LoginUser loginUser, String usecret);

    List<OrderSecret> GetShopSecret(ShopRequest shop);

    int addordercategory(OrderCategory model, LoginUser loginUser);

    int editrdercategory(OrderCategory model);

    List<OrderCategoryResponseDTO> GetOrderCateGory(OrderCategory model);

    List<OrderCategory> GetOrderCateGoryChild(OrderCategory model);

    int addordergood(OrderGoods model, LoginUser loginUser);

    int GetOrderGoodsSort(OrderGoods model);

    int editordergood(OrderGoods model, LoginUser loginUser);

    List<OrderGoods> GetOrderGoods(OrderGoodsRequestDTO model);

    int editsecret(OrderSecret model);

    int addordergoodspec(OrderGoodSpecification ogs);

    int editordergoodspec(OrderGoodSpecification ogs);

    List<OrderGoodSpecification> GetOrderGoodSpecList(OrderGoods orderGoods);

    OrderGoods getOrderGoodsForCheck(OrderGoods model);


}
