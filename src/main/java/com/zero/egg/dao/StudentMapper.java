package com.zero.egg.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.zero.egg.model.Student;

@Mapper
@Repository
public interface StudentMapper {
    Student getStudentByID(int id);
}
