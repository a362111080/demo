package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zero.egg.enums.BrokenGoodsEnums;
import com.zero.egg.tool.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-05-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_broken_goods")
@ApiModel(value="BrokenGoods对象", description="")
public class BrokenGoods extends PageDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",hidden=true)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "所属店铺",required=false)
    private String shopId;

    @ApiModelProperty(value = "所属企业",required=false)
    private String companyId;

    @ApiModelProperty(value = "客户主键",required=false)
    private String customerId;

    @ApiModelProperty(value = "规格主键",hidden=true)
    private String specificationId;

    @ApiModelProperty(value = "任务主键",required=false)
    private String taskId;

    @ApiModelProperty(value = "商品分类主键",hidden=true)
    private String goodsCategroyId;

    @ApiModelProperty(value = "商品编码",required=false)
    private String goodsNo;

    @ApiModelProperty(value = "标记",hidden=true)
    private String marker;

    @ApiModelProperty(value = "记重方式",hidden=true)
    private String mode;

    @ApiModelProperty(value = "重量",hidden=true)
    private BigDecimal weight;

    @ApiModelProperty(value = "业务员",required=false)
    private String userId;

    @ApiModelProperty(value = "出货账单编号，自损无该字段信息",required=false)
    private String billNo;

    @ApiModelProperty(value = "损坏商品编码",required=false)
    private String brokenGoodsNo;

    @ApiModelProperty(value = "类型",required=false)
    private String type;
    

    @ApiModelProperty(value = "售后处理商品编码，自损无该字段信息",required=false)
    private String changeGoodsNo;

    @ApiModelProperty(value = "换货商品状态",required=true)
    private String status;
    
    @ApiModelProperty(value = "备注",hidden=true)
    private String remark;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @TableField(exist = false)
    @ApiModelProperty(value = "创建人名称",hidden=true)
    private String creatorname;


    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createtime;

    @ApiModelProperty(value = "修改人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "修改时间",hidden=true)
    private Date modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;

    @TableField(exist = false)
    @ApiModelProperty(value = "客商名称",hidden=true)
    public String customername;


    @TableField(exist = false)
    @ApiModelProperty(value = "品种名称",hidden=true)
    public String categroyname;


    @TableField(exist = false)
    @ApiModelProperty(value = "显示标记",hidden=true)
    public String brokenmarker;

    @TableField(exist = false)
    @ApiModelProperty(value = "账单id",hidden=true)
    public String billid;

    @TableField(exist = false)
    @ApiModelProperty(value = "二维码id",hidden=true)
    public String barcodeid;



    public String getStatusName() {
        return	BrokenGoodsEnums.Status.note(Integer.parseInt(this.status));

	}

    public String getTypeName() {

        return	BrokenGoodsEnums.Type.note(Integer.parseInt(this.type));


    }

}
