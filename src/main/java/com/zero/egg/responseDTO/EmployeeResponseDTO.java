package com.zero.egg.responseDTO;

import java.io.Serializable;
import java.util.List;
import com.zero.egg.model.Employee;
import com.zero.egg.tool.PageDTO;

import lombok.Data;

/**
 * @ClassName EmployeeListResponseDTO
 * @Author CQ
 * @Date 2018/11/12  
 **/
@Data
public class EmployeeResponseDTO extends PageDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3127302458667720111L;
	
	
    /**
     * 员工列表
     */
    private List<Employee> employeeList;

}
