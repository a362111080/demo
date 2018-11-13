package com.zero.egg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.dao.EmployeeMapper;
import com.zero.egg.model.Employee;
import com.zero.egg.model.EmployeeQuery;
import com.zero.egg.requestDTO.EmployeeRequestDTO;
import com.zero.egg.responseDTO.EmployeeResponseDTO;
import com.zero.egg.service.EmployeeService;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
	/**
	 * @ClassName EmployeeServiceImpl
	 * @Description 员工管理模块ServiceImpl
	 * @Author CQ
	 * @Date 2018/11/5 
	 **/
		@Autowired
	    private EmployeeMapper mapper;

		@Override
	    public Message getEmployee(EmployeeRequestDTO employeeRequestDTO) {
			
			  Message message = new Message();
		      Page<Employee> page = new Page<>();
		      EmployeeResponseDTO  employeeResponseDTO=new EmployeeResponseDTO();
		      try {
		    	    page.setCurrent(employeeRequestDTO.getCurrent());
		            page.setSize(employeeRequestDTO.getSize());
		            page= (Page<Employee>)EmployeeMapper.getEmployee(employeeRequestDTO);         
		            employeeResponseDTO.setEmployeeList(page.getRecords());
		            employeeResponseDTO.setCurrent(page.getCurrent());
		            employeeResponseDTO.setPages(page.getPages());
		            employeeResponseDTO.setTotal(page.getTotal());
		            employeeResponseDTO.setSize(page.getSize());
		            message.setData(employeeResponseDTO);
		            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			} catch (Exception e) {
				
	            throw new ServiceException(UtilConstants.ResponseMsg.FAILED);
			}
		    return message;
	    }

		@Override
		public int UpdateEmployee(EmployeeQuery query) {
			
			return mapper.UpdateEmployee(query);
		}
}


