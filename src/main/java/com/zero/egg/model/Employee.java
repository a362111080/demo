package com.zero.egg.model;

import java.util.Date;

import lombok.Data;

/**
 * @ClassName 员工类
 * @Description 员工类
 * @Author CQ
 * @Date 2018/11/5 
 **/

@Data

public class Employee
{

	//员工id 
	  private String id;
	//员工编码
	  private String strUserCode;
	//员工姓名
	  private String strUserName;
	//员工性别
	  private Boolean strUserSex;
	//员工联系方式
	  private String strUserPhone;
	//员工所属店铺
	  private String strStoreCode;
	//出货数量
	  private int strOutStoreNum;
	//卸货数量（单位每车）
	  private int strOutCarNum;
	//完成订单数量
	  private int strCompleteOrder;
	//员工职称
	  private String strProfession;
	//员工入职时间
	  private Date strEntryTime;
	//员工使用设备  0：Android   1：iOS   
	  private int strEquipmentSource;
	//员工状态
	  private Integer LngState;
	  
}



