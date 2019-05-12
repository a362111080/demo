package com.zero.egg.requestDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ShipmentGoodBarCodeRequestDTO
 * @Author lyming
 * @Date 2019/5/10 15:58
 **/
@Data
public class ShipmentGoodBarCodeRequestDTO implements Serializable {

    private static final long serialVersionUID = 7570645361866521504L;

    /**
     * 二维码信息
     */
    @ApiModelProperty(value = "二维码信息")
    private String barCodeString;

    /**
     * 出货任务主键
     */
    @ApiModelProperty(value = "出货任务主键")
    private String taskId;

    @ApiModelProperty(value = "客户主键")
    private String customerId;
}
