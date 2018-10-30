package com.zero.egg.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Student {

    /**
     * 学生id
     */
    private int id;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生性别
     */
    private String sex;

    /**
     * 学生年级
     */
    private int age;
 


}
