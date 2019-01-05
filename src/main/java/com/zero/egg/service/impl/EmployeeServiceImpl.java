package com.zero.egg.service.impl;

import java.util.List;

import com.zero.egg.requestDTO.ActiveCodeRequestDTO;
import com.zero.egg.requestDTO.EmployeeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.egg.dao.EmployeeMapper;
import com.zero.egg.model.Employee;
import com.zero.egg.service.EmployeeService;
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
	    public List<Employee> getEmployee(EmployeeRequestDTO query) {
	    	return  mapper.getEmployee(query);
	    }

		@Override
		public int UpdateEmployee(EmployeeRequestDTO query) {
			
			return mapper.UpdateEmployee(query);
		}

    @Override
    public int GetCertificatecode(String certificatecode) {

		return mapper.getCertificatecode(certificatecode);
    }

	@Override
	public int ActiveEmployee(ActiveCodeRequestDTO activeModel) {

		return mapper.ActiveEmployee(activeModel);
	}
}


