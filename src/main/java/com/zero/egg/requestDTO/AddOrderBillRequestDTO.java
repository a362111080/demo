package com.zero.egg.requestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author lym
 */
@Data
public class AddOrderBillRequestDTO implements Serializable {

    private static final long serialVersionUID = 7220746841507139636L;

    @NotBlank(message = "店铺信息不能为空")
    private String shopId;

    private String userId;

    @NotEmpty(message = "购物车商品id不能为空")
    private List<String> cartIds;
}
