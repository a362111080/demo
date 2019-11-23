package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.tool.UtilConstants;
import lombok.Data;

/**
 * 设备信息
 *
 * @ClassName DeviceData
 * @Author lyming
 * @Date 2018/11/20 21:39
 **/
@Data
@TableName(value = "sms_device_data")
public class DeviceData {

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 设备序列号
     */
    private String strSerialNumber;

    /**
     * 设备名
     */
    private String strDeviceName;

    /**
     * 绑定称的序列号
     */
    private String strBindingSequence;

    /**
     * 状态 1:运行中 2 :未连接(默认) 3:断开
     */
    private Integer lngState = UtilConstants.DeviceState.UNCONNECTED;
}
