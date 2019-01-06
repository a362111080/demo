package com.zero.egg.dao;

import java.util.List;

import com.zero.egg.requestDTO.ActiveCodeRequestDTO;
import com.zero.egg.requestDTO.EmployeeRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.zero.egg.model.Employee;

/**
 * @Description 员工管理mapper
 * @Author CQ
 * @Date 2018/11/5 19:22
 **/


@Mapper
@Repository
public interface EmployeeMapper {
   
	List<Employee> getEmployee(EmployeeRequestDTO query);

	int UpdateEmployee(EmployeeRequestDTO query);

	int getCertificatecode(String certificatecode);

    int ActiveEmployee(ActiveCodeRequestDTO activeModel);
}
