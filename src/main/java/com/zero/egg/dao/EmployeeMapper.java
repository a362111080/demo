package com.zero.egg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.zero.egg.model.Employee;
import com.zero.egg.model.EmployeeQuery;

/**
 * @Description 员工管理mapper
 * @Author CQ
 * @Date 2018/11/5 19:22
 **/


@Mapper
@Repository
public interface EmployeeMapper {
   
	List<Employee> getEmployee(EmployeeQuery query);

	int UpdateEmployee(EmployeeQuery query);
}
