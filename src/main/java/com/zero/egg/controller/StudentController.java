package com.zero.egg.controller;

import com.zero.egg.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class StudentController {
	@Autowired
    private StudentService studentService;
 
    @RequestMapping("/demo")
    public String get(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        System.out.println(sdf.format(date));
        return "success";
    }
 
    @RequestMapping(value = "res")
    public String df(){
        return "Hello";
    }
}
