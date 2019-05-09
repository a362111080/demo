package com.zero.egg.responseDTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

@Data
public class UnLoadResponseDto implements Serializable {

    private static final long serialVersionUID = -1406006311170704356L;

    //返回标识
    @ApiModelProperty(value = "返回标识")
    private String marker;
    //净重去皮数值，没有则为0
    @ApiModelProperty(value = "净重去皮数值，没有则为0")
    private BigDecimal numerical;

    //返回标识
    @ApiModelProperty(value = "对应规格编码")
    private String specificationId;


    //返回标识
    @ApiModelProperty(value = "方式")
    private String mode;

}
