package com.zero.egg.requestDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 规格方案RequestDTO
 *
 * @ClassName SpecificationProgramRequestDTO
 * @Author lyming
 * @Date 2018/11/9 13:49
 **/
@Data
@ApiModel(value="规格方案RequestDTO对象", description="")
public class SpecificationProgramRequestDTO implements Serializable {

    private static final long serialVersionUID = 3598140603310947570L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键",required=false)
    private String id;

    /**
     * 方案名称
     */
    @ApiModelProperty(value = "方案名称",required=false)
    private String name;

    /**
     * 品种id
     */
    @ApiModelProperty(value = "品种id",required=false)
    private String categoryId;

    /**
     * 删除标识 0:未删除(默认) 1:已删除
     */
    @ApiModelProperty(value = "删除标识 0:未删除(默认) 1:已删除",required=false,example = "0")
    private Integer dr = 0;

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
     * 创建人
     */
    @ApiModelProperty(value = "创建人",required=false)
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",required=false)
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
     * 是否称重 0:不称重 1:称重
     */
    private Integer isWeight ;
}
