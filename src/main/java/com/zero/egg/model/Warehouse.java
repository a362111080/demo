package com.zero.egg.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author hhfeng
 * @Title: Warehouse 
 * @Description:  
 * @date 2018年11月5日
 */

@ApiModel(value="warehouse对象",description="仓库对象")
@Data
@ToString
public class Warehouse  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6449231837925370601L;
	/**
	 * 仓库id
	 */
	@ApiModelProperty(name="warehouseId",dataType="string",value="仓库id（新增时后台生成）",notes="新增时由后台生成")
	private String warehouseId;
	/**
	 * 仓库code
	 */
	@ApiModelProperty(name="warehouseCode",dataType="string",value="仓库code",example="1-2")
	private String warehouseCode;
	/**
	 * 仓库名
	 */
	@ApiModelProperty(name="warehouseName",dataType="string",value="仓库名",example="一号仓库")
	private String warehouseName;
	/**
	 * 店铺code
	 */
	@ApiModelProperty(name="storeId",dataType="string",value="店铺id")
	private String storeId;
	/**
	 * 备注
	 */
	@ApiModelProperty(name="strdesc",dataType="string",value="备注")
	private String strdesc;
	/**
	 * 状态（1启用，-1不启用）
	 */
	@ApiModelProperty(name="lngState",dataType="int",value="状态（1启用，-1不启用）")
	private int lngState;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(name="createdate",dataType="date",value="创建时间")
	private Date createdate;
	/**
	 * 创建人
	 */
	@ApiModelProperty(name="creater",dataType="string",value="创建人")
	private String creater;
	
	
	
	
}
