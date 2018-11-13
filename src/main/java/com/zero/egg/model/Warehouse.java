package com.zero.egg.model;

import java.util.Date;

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
public class Warehouse {

	//id
	private String id;
	//仓库code
	private String warehouse_code;
	//仓库名
	private String warehouse_name;
	//店铺code
	private String store_id;
	//备注
	private String strdesc;
	//状态
	private int lng_state;
	//创建时间
	private Date createdate;
	//创建人
	private String creater;
	
}
