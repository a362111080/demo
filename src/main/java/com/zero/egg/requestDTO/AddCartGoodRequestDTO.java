package com.zero.egg.requestDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName AddCartGoodRequestDTO
 * @Author lyming
 * @Date 2019/9/30 3:35 下午
 **/
@Data
public class AddCartGoodRequestDTO implements Serializable {

    private static final long serialVersionUID = -4682782421135772112L;

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

}
