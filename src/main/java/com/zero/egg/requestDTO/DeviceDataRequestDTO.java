package com.zero.egg.requestDTO;

import com.zero.egg.tool.UtilConstants;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DeviceDataRequestDTO
 * @Author lyming
 * @Date 2018/11/20 22:14
 **/
@Data
public class DeviceDataRequestDTO implements Serializable {

    private static final long serialVersionUID = 4638712578688187847L;

    /**
     * 主键id
     */
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

    /**
     * 前端传来的id数组(批量操作)
     */
    private List<String> ids;

}
