package com.zero.egg.requestDTO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName UpdateCartGoodsCheckRequestDTO
 * @Description 购物车商品是否被选择
 * @Author lyming
 * @Date 2019/10/6 9:38 下午
 **/
@Data
@Accessors(chain = true)
public class UpdateCartGoodsCheckRequestDTO implements Serializable {

    private static final long serialVersionUID = 4504013788752786389L;

    @NotBlank(message = "店铺id不能为空")
    private String shopId;

    /**
     * 当前登录用户id
     */
    private String userId;

    @NotBlank(message = "购物车商品id不能为空")
    private String cartId;

    @NotNull(message = "是否勾选的状态不能为空")
    private Boolean checked;
}
