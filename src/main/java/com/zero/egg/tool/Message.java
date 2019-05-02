package com.zero.egg.tool;

import lombok.Data;

import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author hhfeng
 * @Title: Message 
 * @Description:返回对象实体  
 * @date 2018年11月5日
 */
@Data
public class Message<T> {

	/**
	 * 返回状态 1成功  -1失败  
	 */
	 @ApiModelProperty(value = "返回状态 1成功  -1失败 ")
	private int state;
	/**
	 * 返回信息
	 */
	 @ApiModelProperty(value = "返回信息")
	private String message;
	/**
	 * 返回对象
	 */
	 @ApiModelProperty(value = "返回对象")
	private T data ;
	/**
	 * 返回map
	 */
	private Map<String, Object> map;
	
	
}
