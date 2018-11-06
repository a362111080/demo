package com.zero.egg.tool;

import lombok.Data;

/**
 * 分页对象,给DTO用来继承
 *
 * @ClassName PageDTO
 * @Author lyming
 * @Date 2018/11/6 15:30
 **/
@Data
public class PageDTO {

    /**
     * 当前页
     */
    private Long current = 1L;

    /**
     * 每页数量
     */
    private Long size = 10L;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;


}
