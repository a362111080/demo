package com.zero.egg.controller;

import com.zero.egg.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zero.egg.model.Student;

@RestController
public class StudentController {
	@Autowired
    private StudentService studentService;
 
    @RequestMapping("/demo")
    public Student get(){
        Student student=studentService.getStudentByID(12);
        return  student;
    }
 
    @RequestMapping(value = "res")
    public String df(){
        return "Hello";
    }
}
