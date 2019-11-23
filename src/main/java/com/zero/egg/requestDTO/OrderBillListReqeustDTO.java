package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lym
 */
@Data
public class OrderBillListReqeustDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = -2333639924276345425L;

    /**
     * 订货平台用户id
     */
    private String userId;

    /**
     * 订单状态
     */
    private Integer status;
}
