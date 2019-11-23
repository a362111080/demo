package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "bd_citys")
@ApiModel(value="city对象", description="")
public class city {
    /**
     * @ClassName 省市区类
     * @Description 省市区类
     * @Author CQ
     * @Date 2019/03/06
     **/

    /**
     * 省市区id
     */
    @ApiModelProperty(value = "省市区id",required=false)
    private  String id;
    /**
     * 省市区父级id
     */
    @ApiModelProperty(value = "省市区父级id",required=false)
    private  String parentId;
    /**
     * 省市区名称
     */
    @ApiModelProperty(value = "省市区名称",required=false)
    private  String name;
    /**
     * 省市区分类层级
     */
    @ApiModelProperty(value = "省市区分类层级",required=false)
    private  int LevelType;
}
