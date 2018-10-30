package com.zero.egg.service.impl;

import com.zero.egg.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.egg.dao.StudentMapper;
import com.zero.egg.model.Student;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
	@Autowired
    private StudentMapper mapper;

	@Override
    public Student getStudentByID(int id) {
    	return mapper.getStudentByID(id);
    }
}
