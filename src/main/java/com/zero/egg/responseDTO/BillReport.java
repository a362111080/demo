package com.zero.egg.responseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BillReport {

    @ApiModelProperty(value = "总收入")
    private BigDecimal InCount=BigDecimal.ZERO;

    @ApiModelProperty(value = "总支出")
    private BigDecimal OutCount=BigDecimal.ZERO;

    @ApiModelProperty(value = "总进货数量按品种")
    private List<CategorySum> InCategorySum;

    @ApiModelProperty(value = "总出货数量按品种")
    private List<CategorySum> OutCategorySum;

    @ApiModelProperty(value = "已结清数量")
    private BigDecimal CompleteCount=BigDecimal.ZERO;

    @ApiModelProperty(value = "未结清数量")
    private BigDecimal UnCompleteCount=BigDecimal.ZERO;
}


