package com.zero.egg.responseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName BlankBillResponseDTO
 * @Author lyming
 * @Date 2019/5/20 20:39
 **/
@Data
public class BlankBillResponseDTO implements Serializable {

    private static final long serialVersionUID = 3420993517274939392L;

    @ApiModelProperty(value = "应收金额", required = false)
    private BigDecimal amount;

    @ApiModelProperty(value = "实收金额", required = false)
    private BigDecimal realAmount;

    @ApiModelProperty(value = "用户计重习惯(1:包->按箱收费 2:去皮->按斤收费)", required = false)
    private Integer weightMode;


    private List<BlankBillDTO> blankBillDTOList;
}
