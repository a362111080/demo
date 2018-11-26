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
	public String QueryCode;
	//查询员工名称
	public String QueryName;
	//员工状态
	public int QueryLngState;
	//查询员工绩效开始时间
	public String QueryBeginTime;
	//查询员工绩效结束时间
	public String QueryEndTime;
	//当前店铺编码
	public String QueryStoreCode;
	

}
