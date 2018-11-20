package com.zero.egg.service;

import com.zero.egg.requestDTO.DeviceDataRequestDTO;
import com.zero.egg.tool.Message;

/**
 * 设备信息Service
 *
 * @ClassName DeviceDataService
 * @Author lyming
 * @Date 2018/11/20 22:22
 **/
public interface DeviceDataService {

    /**
     * 新增保存设备信息
     *
     * @Param [deviceDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message saveDeviceData(DeviceDataRequestDTO deviceDataRequestDTO);

    /**
     * 批量删除设备信息(单个删除也走该流程)
     *
     * @Param [deviceDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message deleteDeviceData(DeviceDataRequestDTO deviceDataRequestDTO);

    /**
     * 根据id更新设备信息
     *
     * @Param [deviceDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message updateDeviceDataById(DeviceDataRequestDTO deviceDataRequestDTO);

    /**
     * 设备信息列表(包含根据设备名称模糊搜索)
     *
     * @Param [deviceDataRequestDTO]
     * @Return com.zero.egg.tool.Message
     **/
    public Message listDeviceData(DeviceDataRequestDTO deviceDataRequestDTO);

}
