package com.zero.egg.requestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName DeleteCompletedBillReqeustDTO
 * @Description 删除已经完成的订单
 * @Author lyming
 * @Date 2019/10/11 11:23 上午
 **/
@Data
public class DeleteCompletedBillReqeustDTO implements Serializable {

    private static final long serialVersionUID = 643107935503300351L;

    @NotBlank(message = "店铺id不能为空")
    private String shopId;

    @NotBlank(message = "订单id不能为空")
    private String orderId;

    /**
     * 登录用户的id
     */
    private String userId;
}
