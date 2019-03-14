package com.zero.egg.api.dto;

import lombok.Data;

/**
 * @author hhfeng
 * @Title: Message 
 * @Description:返回对象实体  
 * @date 2018年11月5日
 */
@Data
public class BaseResponse<T> {
	
	public BaseResponse(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public BaseResponse(){
		
	}

	/**
	 * 返回状态  
	 */
	public int code;
	/**
	 * 返回信息
	 */
	public String msg;
	/**
	 * 返回对象
	 */
	public T data ;
	
	
}
