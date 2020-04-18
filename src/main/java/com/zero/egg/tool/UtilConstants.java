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
        /**
         * 任务已经被取消
         */
        public static final int TASK_CANCLED = -4;
        /**
         * 任务已经完成
         */
        public static final int TASK_FINISHED = -5;
        /**
         * 正常出货任务已经存在
         */
        public static final int NORMAL_TAST_EXIST = -6;
        /**
         * 订货平台出货任务已经存在
         */
        public static final int ORDER_TAST_EXIST = -7;

        /**
         * 订货平台出货任务已经存在
         */
        public static final int UNLOAD_TYPE_ERROR = -8;

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
        public static final String TAST_EXIST = "该合作商有活动中的任务";

        /**
         * 有活动中的任务
         */
        public static final String ORDER_TAST_EXIST = "该合作商有活动中的订货任务";
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
        /**
         * 该账单不为空
         */
        public static final String NOT_BLANK_BILL = "该账单不为空";
        /**
         * 任务已经被取消
         */
        public static final String TASK_CANCLED_MSG = "任务已经被取消";
        /**
         * 任务已经完成
         */
        public static final String TASK_FINISHED_MSG = "任务已经完成";
        /**
         * 任务已经完成
         */
        public static final String RETAIL_ONE_ONLY = "零售只能一次出一件货";
        /**
         * 非零售合作商或者查不到相关零售合作商
         */
        public static final String NOT_RETAIL_CUSTOMER = "非零售合作商或者查不到相关零售合作商";
        /**
         * 该货物在其他未结束出货任务里
         */
        public static final String IN_OTHER_TASK = "该货物在其他未结束出货任务里";
        /**
         * 没有此秘钥信息
         */
        public static final String NO_SUCH_SECRET = "没有此秘钥信息";
        /**
         * 用户在该店铺下已经绑定过,不用重复绑定
         */
        public static final String SHOP_BINDED = "用户在该店铺下已经绑定过,不用重复绑定";
        /**
         * 秘钥已经被使用
         */
        public static final String SECRET_HAS_BEEN_USED = "秘钥已经被使用";
        /**
         * 没有合作的店铺信息
         */
        public static final String NO_COOPERATE_SHOP = "没有合作的店铺信息";
        /**
         * 方案已经被使用,不能被修改,但是可以删除,不影响历史数据
         */
        public static final String SPECIFICATION_USED = "方案已经被使用,不能被修改,但是可以删除,不影响历史数据";
        /**
         * 任务中有货物已经报损,请移除后结束任务
         */
        public static final String BROKEN_GOODS_IN_TASK = "任务中有货物已经报损,请移除后结束任务";
        /**
         * 存在已经报损的货物,不能取消账单
         */
        public static final String BROKEN_GOODS_IN_BILL = "存在已经报损的货物,不能取消账单";
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
        /**
         * 用户信息前缀
         */
        public static final String USER_REDIS = "user-redis:";
    }

    /**
     * 用户类型
     */
    public static class UserType {
        /**
         * 企业端
         */
        public static final Integer Company = 0;
        /**
         * PC客户端
         */
        public static final Integer Pc = 1;
        /**
         * Boss移动端
         */
        public static final Integer Boss = 2;
        /**
         * 员工端
         */
        public static final Integer Staff = 3;
        /**
         * 设备端
         */
        public static final Integer Device  = 4;
        /**
         * SAAS平台端
         */
        public static final Integer SAAS = 5;
        /**
         * 订货平台端
         */
        public static final Integer Order = 6;
    }

}
