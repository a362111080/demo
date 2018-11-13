package com.zero.egg.service;

import com.zero.egg.model.EmployeeQuery;
import com.zero.egg.requestDTO.EmployeeRequestDTO;
import com.zero.egg.tool.Message;;

public interface EmployeeService {
	//查询员工信息
    Message getEmployee(EmployeeRequestDTO  employeeRequestDTO );
	//更新员工状态
	int UpdateEmployee(EmployeeQuery query);
}
