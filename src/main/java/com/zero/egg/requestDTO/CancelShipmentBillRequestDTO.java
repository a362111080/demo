package com.zero.egg.requestDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CancelShipmentBillRequestDTO
 * @Description 取消出货订单RequestDTO
 * @Author lyming
 * @Date 2019-07-26 23:14
 **/
@Data
public class CancelShipmentBillRequestDTO implements Serializable {

    private static final long serialVersionUID = -3434703098076146121L;

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "企业id")
    private String companyId;

    @ApiModelProperty(value = "企业id")
    private String shopId;

}
