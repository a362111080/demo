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

        /**
         * 员工未结束
         */
        public static final int EMPLOYEE_NOT_FINISH = -2;
        /**
         * 特定出货任务,出货商品为null
         */
        public static final int SHIPMENTGOODS_NULL = -3;
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
         * 凭证校验失败
         */
        public static final String HTTPAPI_ERROR = "凭证校验失败";

        /**
         * session_key过期
         */
        public static final String SESSION_KEY_TIMEOUT = "session_key过期";


        /**
         * 生成编码成功
         */
        public static final String GENERATE_CODE_SUCCESS = "生成编码成功";

        /**
         * 有活动中的任务
         */
        public static final String TAST_EXIST = "有活动中的任务";
        /**
         * 查无此货
         */
        public static final String NO_SUCH_GOOD = "查无此货";
        /**
         * 该任务已结束或暂停
         */
        public static final String TASK_NOT_FOUND = "该任务已结束或暂停";
        /**
         * 该任务已结束或已取消
         */
        public static final String TASK_FINISH_OR_CANCELED = "该任务已结束或已取消";

        /**
         * 该任务已结束或已取消或者已经完成(员工端的完成等同于暂停任务)
         */
        public static final String TASK_FINISH_OR_CANCELED_OR_UNEXECUTED = "该任务已结束或已取消或者已经完成";

        /**
         * 该设备无权限
         */
        public static final String NO_PERMISSION = "该设备无权限";

        /**
         * 请确认员工已经完成任务
         */
        public static final String ASURE_EMPLOYEE_FINISH = "请确认员工已经完成任务";
        /**
         * 该任务商品为空,建议直接取消任务
         */
        public static final String NULL_SHIPMENTGOODS = "该任务商品为空,建议直接取消任务";
    }

    /**
     * @author hhf
     * Title: WarehouseState
     * @date 2018年11月19日
     * Description: 仓库状态
     */
    public static class WarehouseState {
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
     * Title: GoodsState
     * @date 2018年11月19日
     * Description: 商品状态
     */
    public static class GoodsState {
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

    /**
     * 微信状态
     */
    public static class WXState {
        //成功
        public static final int OK = 200;
        //失败
        public static final int FAIL = 500;
    }

    /**
     * Redis的key前缀
     */
    public static class RedisPrefix {
        /**
         * 微信用户信息(openid)前缀
         */
        public static final String WXUSER_REDIS_SESSION = "wxuser-redis-session:";

        /**
         * 出货任务前缀
         */
        public static final String SHIPMENTGOOD_TASK = "shipmentgood-task:";
    }

}
