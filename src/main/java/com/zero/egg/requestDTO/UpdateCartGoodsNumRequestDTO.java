package com.zero.egg.requestDTO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName UpdateCartGoodsNumRequestDTO
 * @Description 更新购物车商品数量
 * @Author lyming
 * @Date 2019/10/6 9:30 下午
 **/
@Data
@Accessors(chain = true)
public class UpdateCartGoodsNumRequestDTO implements Serializable {

    private static final long serialVersionUID = 2554019918491241667L;

    @NotBlank(message = "店铺id不能为空")
    private String shopId;

    /**
     * 当前登录用户id
     */
    private String userId;

    @NotBlank(message = "购物车商品id不能为空")
    private String cartId;

    @NotNull(message = "商品数量不能为空")
    private Integer number;
}
