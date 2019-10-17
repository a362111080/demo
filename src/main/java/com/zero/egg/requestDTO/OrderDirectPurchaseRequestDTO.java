package com.zero.egg.requestDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName OrderDirectPurchaseRequestDTO
 * @Description 商品直接购买RequestDTO
 * @Author lyming
 * @Date 2019/10/17 4:02 下午
 **/
@Data
@Accessors(chain = true)
public class OrderDirectPurchaseRequestDTO implements Serializable {

    private static final long serialVersionUID = 554372081585271782L;

    /**
     * 用户id
     */
    private String userId;

    @NotNull(message = "商品id不能为空")
    private String goodsId;

    @NotNull(message = "店铺id不能为空")
    private String shopId;

    @ApiModelProperty(value = "称重方式 1:去皮->按斤收费 2:包->按箱收费")
    @NotNull(message = "收费方式不能为null")
    private String weightMode;

    @NotNull(message = "规格id不能为空")
    private String goodSpecificationId;

    @NotNull(message = "数量不能为空")
    private Integer number;

    @NotBlank(message = "地址id不能为空")
    private String addressId;

    /**
     * 用户留言
     */
    private String message;

}
