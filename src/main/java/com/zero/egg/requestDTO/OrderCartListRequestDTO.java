package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.Serializable;

/**
 * @ClassName OrderCartListRequestDTO
 * @Description 获取购物车列表
 * @Author lyming
 * @Date 2019/10/6 3:11 下午
 **/
@Data
public class OrderCartListRequestDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = 317959255951517072L;

    /**
     * 店铺id
     */
    @NotBlank(message = "店铺id不能为空")
    private String shopId;
    /**
     * 登录用户id
     */
    private String userId;
}
