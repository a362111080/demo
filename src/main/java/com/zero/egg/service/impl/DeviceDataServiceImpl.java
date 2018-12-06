package com.zero.egg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zero.egg.dao.DeviceMapper;
import com.zero.egg.model.DeviceData;
import com.zero.egg.requestDTO.DeviceDataRequestDTO;
import com.zero.egg.responseDTO.DeviceDataResponseDTO;
import com.zero.egg.service.DeviceDataService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.TransferUtil;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName DeviceDataServiceImpl
 * @Author lyming
 * @Date 2018/11/20 22:31
 **/
@Transactional
@Slf4j
@Service
public class DeviceDataServiceImpl implements DeviceDataService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Message saveDeviceData(DeviceDataRequestDTO deviceDataRequestDTO) {
        Message message = new Message();
        DeviceData deviceData = new DeviceData();
        /** 查重结果 */
        List<DeviceData> resultList = null;
        try {
            TransferUtil.copyProperties(deviceData, deviceDataRequestDTO);
            /** 插入数据前做查重操作
             *  strSerialNumber和strDeviceName不能重复
             */
            resultList = deviceMapper.selectList(new QueryWrapper<DeviceData>()
                    .eq("str_serial_number", deviceData.getStrSerialNumber())
                    .or()
                    .eq("str_device_name", deviceData.getStrDeviceName()));
            if (resultList.size() > 0) {
                message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.DUPLACTED_DATA);
            } else {
                deviceMapper.insert(deviceData);
                message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            }
            return message;
        } catch (Exception e) {
            log.info("saveDeviceData error", e);
            throw new ServiceException("saveDeviceData error");

        }
    }

    @Override
    public Message deleteDeviceData(DeviceDataRequestDTO deviceDataRequestDTO) {
        Message message = new Message();
        try {
            deviceMapper.deleteBatchIds(deviceDataRequestDTO.getIds());
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("deleteDeviceData error", e);
            throw new ServiceException("deleteDeviceData error");
        }
    }

    @Override
    public Message updateDeviceDataById(DeviceDataRequestDTO deviceDataRequestDTO) {
        Message message = new Message();
        DeviceData deviceData = new DeviceData();
        try {
            TransferUtil.copyProperties(deviceData, deviceDataRequestDTO);
            deviceMapper.updateById(deviceData);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("updateDeviceDataById error", e);
            throw new ServiceException("updateDeviceDataById error");

        }
    }

    @Override
    public Message listDeviceData(DeviceDataRequestDTO deviceDataRequestDTO) {
        Message message = new Message();
        DeviceDataResponseDTO deviceDataResponseDTO = new DeviceDataResponseDTO();
        List<DeviceData> deviceDataList = null;
        try {
            if ("" != deviceDataRequestDTO.getStrDeviceName() && null != deviceDataRequestDTO.getStrDeviceName()) {
                deviceDataList = deviceMapper.selectList(new QueryWrapper<DeviceData>().like("str_device_name", deviceDataRequestDTO.getStrDeviceName()));
            } else {
                deviceDataList = deviceMapper.selectList(null);
            }
            deviceDataResponseDTO.setDeviceList(deviceDataList);
            message.setData(deviceDataResponseDTO);
            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
            return message;
        } catch (Exception e) {
            log.info("listDeviceData error", e);
            throw new ServiceException("listDeviceData error");
        }

    }
}
