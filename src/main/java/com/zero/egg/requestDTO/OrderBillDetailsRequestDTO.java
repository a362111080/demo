package com.zero.egg.requestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName OrderBillDetailsRequestDTO
 * @Description 获取订货平台订单详情RequestDTO
 * @Author lyming
 * @Date 2019/10/12 6:51 下午
 **/
@Data
public class OrderBillDetailsRequestDTO implements Serializable {

    private static final long serialVersionUID = -274025396057544108L;

    @NotBlank(message = "订单id不能为空")
    private String orderId;

    /**
     * 登录用户的id
     */
    private String userId;
}
