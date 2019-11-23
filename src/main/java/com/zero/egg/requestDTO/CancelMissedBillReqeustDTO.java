package com.zero.egg.requestDTO;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
public class CancelMissedBillReqeustDTO implements Serializable {

    private static final long serialVersionUID = 1592550819745205603L;

    @NotBlank(message = "店铺id不能为空")
    private String shopId;

    @NotBlank(message = "订单id不能为空")
    private String orderId;

    /**
     * 登录用户的id
     */
    private String userId;
}
