package com.zero.egg.model;

import java.util.Date;

import lombok.Data;


/**
 * @ClassName 登录成功返回数据类
 * @Author CQ
 * @Date 2018/11/5 
 **/


@Data
public class LoginInfo {

	  //店铺编码
	  private String strstorecode;
	  //店铺名称
	  private String strstorename;
	  //店铺地址
	  private String strstoreaddress;
	  //店铺电话
	  private String strstorephone;
	  //店铺老板对应用户编码
	  private String strusercode;
	  //店铺老板姓名
	  private String strUserName;
	  //店铺老板性别
	  private Integer strUserSex;
}
