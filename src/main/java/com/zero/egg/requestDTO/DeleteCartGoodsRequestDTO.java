package com.zero.egg.requestDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DeleteCartGoodsRequestDTO
 * @Description 删除购物车商品RequestDTO(支持批量)
 * @Author lyming
 * @Date 2019/10/6 3:29 下午
 **/
@Data
public class DeleteCartGoodsRequestDTO implements Serializable {

    private static final long serialVersionUID = -2334447354358037525L;

    @NotEmpty(message = "至少选择一个商品")
    @ApiModelProperty("待删除的商品id集合")
    private List<String> ids;

    @NotBlank(message = "店铺id不能为空")
    private String shopId;

    private String userId;
}
