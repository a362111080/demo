package com.zero.egg.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.enums.ShopEnums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_shop")
@ApiModel(value="Shop对象", description="")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "编码",required=false)
    private String code;

    @ApiModelProperty(value = "名称",required=false)
    private String name;

    @ApiModelProperty(value = "地址",required=false)
    private String address;

    @ApiModelProperty(value = "公司主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "联系方式",required=false)
    private String phone;
    
    @ApiModelProperty(value = "pc端数量",required=false)
    private Integer pcClient;
    
    @ApiModelProperty(value = "可用pc端数量",hidden=true)
    private Integer addiblePcClient;
    
    @ApiModelProperty(value = "boss端数量",required=false)
    private Integer bossClient;
    
    @ApiModelProperty(value = "可用boss端数量",hidden=true)
    private Integer addibleBossClient;

    @ApiModelProperty(value = "员工端数量",required=false)
    private Integer staffClient;
    
    @ApiModelProperty(value = "可用员工端数量",hidden=true)
    private Integer addibleStaffClient;
    
    @ApiModelProperty(value = "设备端数量",required=false)
    private Integer deviceClient;
    
    @ApiModelProperty(value = "可用设备端数量",hidden=true)
    private Integer addibleDeviceClient;

    @ApiModelProperty(value = "状态",hidden=true)
    private String status;

    @ApiModelProperty(value = "业务员",required=false)
    private String salesman;
    
    @ApiModelProperty(value = "实施员",required=false)
    private String technician;
    
    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private boolean dr;

    @ApiModelProperty(value = "省份",hidden=true)
    private String cityId;

    /**
     * 省市区一级id
     */
    @ApiModelProperty(value = "省市区一级id", required = false)
    @TableField(exist = false)
    private String city1;
    /**
     * 省市区二级id
     */
    @ApiModelProperty(value = "省市区二级id", required = false)
    @TableField(exist = false)
    private String city2;


    public String getStatusName() {
		return ShopEnums.Status.note(Integer.parseInt(this.status));
	}
}
