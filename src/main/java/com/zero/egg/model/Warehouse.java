package com.zero.egg.model;

import java.io.Serializable;
import java.util.Date;

import com.zero.egg.tool.PageDTO;

import lombok.Data;
import lombok.ToString;

/**
 * @author hhfeng
 * @Title: Warehouse 
 * @Description:  
 * @date 2018年11月5日
 */
@Data
@ToString
public class Warehouse  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6449231837925370601L;
	//仓库id
	private String warehouse_id;
	//仓库code
	private String warehouse_code;
	//仓库名
	private String warehouse_name;
	//店铺code
	private String store_id;
	//备注
	private String strdesc;
	//状态（1启用，0不启用）
	private int lng_state;
	//创建时间
	private Date createdate;
	//创建人
	private String creater;
	//查询内容
	private String searchValue;
	
}
