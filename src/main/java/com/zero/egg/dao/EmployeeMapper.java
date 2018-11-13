package com.zero.egg.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.egg.model.Employee;
import com.zero.egg.model.EmployeeQuery;
import com.zero.egg.requestDTO.EmployeeRequestDTO;

/**
 * @Description 员工管理mapper
 * @Author CQ
 * @Date 2018/11/5 19:22
 **/


@Mapper
@Repository
public interface EmployeeMapper {
   
    static Page<Employee> getEmployee(EmployeeRequestDTO employeeRequestDTO) {
		// TODO Auto-generated method stub
		return  getEmployee(employeeRequestDTO);
	}	
	
	int UpdateEmployee(EmployeeQuery query);

}
