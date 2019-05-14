package com.zero.egg.responseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnLoadCountResponseDto {
    private static final long serialVersionUID = 6957017767264523082L;

    //返回标识
    @ApiModelProperty(value = "返回标识")
    private String marker;
    //净重去皮数值，没有则为0
    @ApiModelProperty(value = "净重去皮数值，没有则为0")
    private int Count;



    @ApiModelProperty(value = "鸡蛋品种",hidden=true)
    private String categoryName;

    @ApiModelProperty(value = "店铺名",hidden=true)
    private String supplierName;

    @ApiModelProperty(value = "是否预警",hidden=true)
    private Boolean warn;


}
