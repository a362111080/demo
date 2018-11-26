package com.zero.egg.service;
import java.util.List;

import com.zero.egg.model.Employee;
import com.zero.egg.requestDTO.EmployeeRequestDTO;

public interface EmployeeService {
	//查询员工信息
	List<Employee> getEmployee(EmployeeRequestDTO QueryModel );
	//更新员工状态
	int UpdateEmployee(EmployeeRequestDTO query);
}
