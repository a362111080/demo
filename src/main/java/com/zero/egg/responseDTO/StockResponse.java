package com.zero.egg.responseDTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class StockResponse implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 5974812890795365264L;
	
	
	private String stockId;
	
	private String quantity;
	
	private String specificationId;
	
	private String programId;
	
	private String weightMin;
	
	private String weightMax;
	
	private String marker;
	
	private String mode;
	
	private String numerical;
	
	private String warn;

}
