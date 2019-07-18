package com.zero.egg.requestDTO;

import com.zero.egg.responseDTO.BlankBillDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName BlankBillRequestDTO
 * @Author lyming
 * @Date 2019/5/20 20:39
 **/
@Data
public class BlankBillRequestDTO implements Serializable {


    private static final long serialVersionUID = 8634714243329477777L;

    @ApiModelProperty(value = "应收金额", required = false)
    private BigDecimal amount;

    @ApiModelProperty(value = "实收金额", required = false)
    private BigDecimal realAmount;

    @ApiModelProperty(value = "用户计重习惯(1:去皮->按斤收费 2:包->按箱收费)", required = false)
    private String weightMode;

    private List<BlankBillDTO> blankBillDTOList;
}
