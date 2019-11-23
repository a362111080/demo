package com.zero.egg.requestDTO;

import com.zero.egg.tool.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 鸡蛋类型RequestDTO
 *
 * @ClassName CategoryRequestDTO
 * @Author lyming
 * @Date 2018/11/6 16:25
 **/
@Data
@ApiModel(value="鸡蛋类型RequestDTO对象", description="")
public class CategoryRequestDTO extends PageDTO implements Serializable {


    private static final long serialVersionUID = -1867113411973002512L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键",required=false)
    private String id;

    /**
     * 鸡蛋类别名称
     */
    @ApiModelProperty(value = "鸡蛋类别名称",required=false)
    private String name;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人",required=false)
    private String creator;

    /**
     * 类别创建时间
     */
    @ApiModelProperty(value = "类别创建时间",required=false)
    private Date createtime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人",required=false)
    private String modifier;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间",required=false)
    private Date modifytime;


    /**
     * 所属店铺id
     */
    @ApiModelProperty(value = "所属店铺id",required=false)
    private String shopId;

    /**
     * 店铺所属企业id
     */
    @ApiModelProperty(value = "店铺所属企业id",required=false)
    private String companyId;

    /**
     * 删除标识 0:未删除(默认) 1:已删除
     */
    @ApiModelProperty(value = "删除标识 0:未删除(默认) 1:已删除",required=false,example = "0")
    private Integer dr = 0;

    /**
     * 前端传来的id数组(批量操作)
     */
    @ApiModelProperty(value = "前端传来的类别id数组(批量操作)",required=false)
    private List<String> ids;
}
