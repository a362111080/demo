package com.zero.egg.responseDTO;

import com.zero.egg.model.DeviceData;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DeviceDataResponseDTO
 * @Author lyming
 * @Date 2018/11/20 22:50
 **/
@Data
public class DeviceDataResponseDTO implements Serializable {

    private static final long serialVersionUID = 5814787708114791864L;

    /**
     * 设备信息列表
     */
    private List<DeviceData> deviceList;
}
