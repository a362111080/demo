package com.zero.egg.tool;

/**
 * 常量定义接口
 *
 * @ClassName UtilConstants
 * @Author lyming
 * @Date 2018/11/5 21:44
 **/

public interface UtilConstants {

    /**
     * 公共常量
     */
    public static class Public {

    }


    /**
     * 返回状态常量
     */
    public static class ResponseCode {

        /**
         * 系统处理正常
         */
        public static final int SUCCESS_HEAD = 1;

        /**
         * 系统处理未知异常
         */
        public static final int EXCEPTION_HEAD = -1;
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
    }
    
    /**
     * @author hhf
     *Title: WarehouseState
     *@date 2018年11月19日 
     *Description: 仓库状态
     */
    public static  class WarehouseState {
    	/**
    	 * 启用
    	 */
    	public static final int enabled = 1;
    	/**
    	 * 不启用
    	 */
    	public static final int disable = -1;
	}
    /**
     * @author hhf
     *Title: GoodsState
     *@date 2018年11月19日 
     *Description: 商品状态
     */
    public static class GoodsState{
    	/**
    	 * 正常库存商品
    	 */
    	public static final int inStore = 1;
    	
    	/**
    	 * 损坏
    	 */
    	public static final int damage = -1;
    	/**
    	 * 已售
    	 */
    	public static final int sold = 2;
    	
    }

    /**
     * 设备状态
     */
    public static class DeviceState {

        /**
         * 运行中
         */
        public static final int RUNNING = 1;

        /**
         * 未连接(默认)
         */
        public static final int UNCONNECTED = 2;

        /**
         * 断开
         */
        public static final int DISCONNECT = 3;

    }

}
