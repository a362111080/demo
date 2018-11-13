package com.zero.egg.requestDTO;

import java.io.Serializable;

import com.zero.egg.tool.PageDTO;

import lombok.Data;

/**
 * 员工RequestDTO
 *
 * @ClassName 查询员工RequestDTO
 * @Author CQ
 * @Date 2018/11/12 16:25
 **/
@Data
public class EmployeeRequestDTO extends PageDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -5796039736965954691L;

	//查询员工编码
	private String QueryCode;
	//查询店铺编码
	private String QueryStoreCode;
	//查询员工名称
	private String QueryName;
	//查询员工状态
	private int  QueryLngState;
	//查询员工绩效开始
	private String  QueryBeginTime;
	//查询员工绩效结束时间
	private String  QueryEndTime;
	

}
