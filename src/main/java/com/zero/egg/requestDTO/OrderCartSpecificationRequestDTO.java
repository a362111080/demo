package com.zero.egg.requestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class OrderCartSpecificationRequestDTO implements Serializable {

    private static final long serialVersionUID = -1894448084250859890L;

    /**
     * 店铺id
     */
    @NotBlank(message = "店铺id不能为空")
    private String shopId;
    /**
     * 登录用户id
     */
    private String userId;

    /**
     * 商品Id
     */
    @NotBlank(message = "商品Id不能为空")
    private String goodsId;
}
