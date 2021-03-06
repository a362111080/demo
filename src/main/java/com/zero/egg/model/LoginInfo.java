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

      //登录验证码
	  private String strVerificationCode;
      //登录密码
	  private String strPassword;
	  //登录账号
 	  private String strPassName;
	  //店铺编码
	  private String strstorecode;
	  //店铺名称
	  private String strstorename;
	  //店铺地址
	  private String strstoreaddress;
	  //店铺电话
	  private String strstorephone;
	  //登录用户编码
	  private String strusercode;
	  //登录姓名
	  private String strUserName;
	  //登录性别
	  private Integer strUserSex;
}
