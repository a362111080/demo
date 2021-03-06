package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillRequest extends PageDTO{
	
	    @ApiModelProperty(value = "店铺主键",required=false)
	    private String shopId;

	    @ApiModelProperty(value = "企业主键",required=false)
	    private String companyId;

	    @ApiModelProperty(value = "账单编号",required=false)
	    private String billNo;

	    @ApiModelProperty(value = "客商主键",required=false)
	    private String cussupId;

	    @ApiModelProperty(value = "账单日期",required=false)
	    private Date billDate;

	    @ApiModelProperty(value = "账单类型",required=false)
	    private String type;

	    @ApiModelProperty(value = "数量",required=false)
	    private BigDecimal quantity;

	    @ApiModelProperty(value = "金额",required=false)
	    private BigDecimal amount;

	    @ApiModelProperty(value = "状态",required=true)
	    private String status;

		@ApiModelProperty(value = "查询关键字",required=true)
		private String keyword;

		@ApiModelProperty(value = "开始时间",required=true)
		private String begintime;

		@ApiModelProperty(value = "结束时间",required=true)
		private String endtime;

}
