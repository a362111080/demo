package com.zero.egg.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bd_unload_goods")
@ApiModel(value="UnloadGoods对象", description="")
public class UnloadGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "供应商主键",required=false)
    private String supplierId;

    @ApiModelProperty(value = "方案主键",required=false)
    private String programId;

    @ApiModelProperty(value = "任务主键",required=false)
    private String taskId;

    @ApiModelProperty(value = "商品分类主键",required=false)
    private String goodsCategoryId;

    @ApiModelProperty(value = "商品编码",required=false)
    private String goodsNo;

    @ApiModelProperty(value = "标记",required=false)
    private String marker;

    @ApiModelProperty(value = "记重方式",required=false)
    private String mode;

    @ApiModelProperty(value = "重量",required=false)
    private BigDecimal weight;

    @ApiModelProperty(value = "是否预警",required=false)
    private Boolean warn;

    @ApiModelProperty(value = "备注",required=false)
    private String remark;

    @ApiModelProperty(value = "创建人",hidden=true)
    private String creator;

    @ApiModelProperty(value = "创建时间",hidden=true)
    private LocalDateTime createtime;

    @ApiModelProperty(value = "更新人",hidden=true)
    private String modifier;

    @ApiModelProperty(value = "更新时间",hidden=true)
    private LocalDateTime modifytime;

    @ApiModelProperty(value = "删除标识",hidden=true)
    private Boolean dr;


}
