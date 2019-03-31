package com.zero.egg.requestDTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginUser implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -8239523587361063003L;

	
	    private String id;
	 
	    private String code;
	 
	    private String name;
	 
	    private Integer sex;

	    private String phone;
	
	
}
