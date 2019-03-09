package com.zero.egg.api;

public class ApiConstants {
	
	 /**
     * 返回状态常量
     */
    public static class ResponseCode {
	
	/**
	 * 无权限
	 */
	public final static Integer NOT_POWER = 197;		//无权限
	
	/**
	 * 登陆过期
	 */
	public final static Integer LOGIN_OVERDUE = 198;		//登陆过期
	
	/**
	 * code 服务连接失败 199
	 */
	public final static Integer SERVICE_LINK_ERROR = 199;	//服务连接失败
	
	/**
	 * code 成功  200
	 */
	public final static Integer SUCCESS = 200;				
	
	/**
	 * code 业务通用执行错误 201
	 */
	public final static Integer EXECUTE_ERROR = 201;		//业务通用执行错误
	
	/**
	 * code 空数据 202
	 */
	public final static Integer NULL_DATA = 202;			//空数据
	/**
	 * code 重复数据 203
	 */
	public final static Integer MORE_DATA = 203;			//重复数据
	
	/**
	 * 支付失败
	 */
	public final static Integer PAY_ERROR = 211;			//支付失败
	
	/**
	 * 微信支付
	 */
	public final static Integer WXPAY = 800;				//微信支付
	
	/**
	 * 支付宝支付
	 */
	public final static Integer ALIPAY = 801;				//支付宝支付
	
	//-----
	/**
	 * 默认页面的数据数量 20
	 */
	public final static Integer PAGE_LIMIT = 20;
	
    }
	
    /**
     * 返回消息常量
     */
    public static class ResponseMsg {

        /**
         * 系统处理正常
         */
        public static final String SUCCESS = "成功";

        /**
         * 系统处理失败
         */
        public static final String FAILED = "处理异常";

        /**
         * 至少选择一个选项
         */
        public static final String ATLEAST_ONE = "至少选择一条记录";


        /**
         * 有重复数据
         */
        public static final String DUPLACTED_DATA = "有重复数据";

        /**
         * 缺少参数
         */
        public static final String PARAM_MISSING = "缺少参数";

        /**
         * 参数错误
         */
        public static final String PARAM_ERROR = "参数错误";
        /**
         *空数据
         */
        public static final String NULL_DATA = "空数据";
        
        /**
         * 业务错误
         */
        public static final String EXECUTE_ERROR="业务错误";
    }
}
