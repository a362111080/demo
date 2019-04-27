package com.zero.egg.tool;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "当前页",required = false,example = "1")
    private Long current = 1L;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量",required = false,example = "10")
    private Long size = 10L;

    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数",required = false)
    private Long total;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数",required = false)
    private Long pages;


}
