package com.zero.egg.service;
import java.util.List;
import com.zero.egg.model.Employee;
import com.zero.egg.model.EmployeeQuery;;

public interface EmployeeService {

	List<Employee> getEmployee(EmployeeQuery  QueryModel );

	int UpdateEmployee(EmployeeQuery query);
}
