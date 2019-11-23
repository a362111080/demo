package com.zero.egg.requestDTO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName UpdateCartGoodsRequestDTO
 * @Description 更新购物车商品信息
 * @Author lyming
 * @Date 2019/10/6 8:50 下午
 **/
@Data
@Accessors(chain = true)
public class UpdateCartGoodsSpecificationRequestDTO implements Serializable {

    private static final long serialVersionUID = -7242696343466459628L;

    @NotBlank(message = "店铺id不能为空")
    private String shopId;

    /**
     * 当前登录用户id
     */
    private String userId;

    @NotBlank(message = "购物车商品id不能为空")
    private String cartId;

    @NotBlank(message = "商品id不能为空")
    private String goodsId;

    /**
     * 合作商计重习惯(1:去皮->按斤收费 2:包->按箱收费)
     */
    @NotBlank(message = "计算方式不能为空")
    private String weightMode;

    @NotBlank(message = "商品规格不能为空")
    private String goodSpecificationId;
}
