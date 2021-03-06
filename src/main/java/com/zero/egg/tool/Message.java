package com.zero.egg.tool;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

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
	 * 返回对象2
	 */
	@ApiModelProperty(value = "返回对象")
	private T totaldata ;
	/**
	 * 返回map
	 */
	private Map<String, Object> map;

	public Message(int state, String message) {
		this.state = state;
		this.message = message;
	}

	public Message() {
	}
}
