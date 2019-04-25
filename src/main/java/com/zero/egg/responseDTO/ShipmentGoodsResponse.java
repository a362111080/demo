package com.zero.egg.responseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ShipmentGoodsResponse {
	
	    private String id;

	    private String shopId;

	    private String companyId;

	    private String customerId;

	    private String specificationId;

	    private String taskId;

	    private String goodsCategoryId;

	    private String goodsNo;

	    private String marker;
	    
	    private String mode;

	    private BigDecimal weight;

	    private String remark;

	    private String creator;

	    private LocalDateTime createtime;

	    private String modifier;

	    private LocalDateTime modifytime;

	    private Boolean dr;
	    
	    private String programName;
	    
	    private String programId;
	    
	    private String categoryName;
	    
	    private String categoryId;
	    
	    private String operator;
	    
	    private Integer count;

}
