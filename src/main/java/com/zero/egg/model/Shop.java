package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.enums.CompanyEnums;
import com.zero.egg.enums.ShopEnums;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
    
    @ApiModelProperty(value = "boss端数量",required=false)
    private Integer bossClient;
    
    @ApiModelProperty(value = "员工端数量",required=false)
    private Integer staffClient;
    
    @ApiModelProperty(value = "设备端数量",required=false)
    private Integer deviceClient;

    @ApiModelProperty(value = "状态",hidden=true)
    private String status;

    @ApiModelProperty(value = "业务员",required=false)
    private String salesman;
    
    @ApiModelProperty(value = "实施员",required=false)
    private String technician;
    
    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private LocalDateTime createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private LocalDateTime modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private boolean dr;

    public String getStatusName() {
		return ShopEnums.Status.note(Integer.parseInt(this.status));
	}
}
