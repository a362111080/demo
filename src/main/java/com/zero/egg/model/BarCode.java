package com.zero.egg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "bd_barcode")
@ApiModel(value="barcode对象", description="")
public class BarCode implements Serializable {

    private static final long serialVersionUID = -3485701086348884048L;
    /**
     * @ClassName 条码类
     * @Description 条码类
     * @Author CQ
     * @Date 2019/03/15
     **/
    @ApiModelProperty(value = "主键",required=false)
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "店铺主键",required=false)
    private String shopId;

    @ApiModelProperty(value = "企业主键",required=false)
    private String companyId;

    @ApiModelProperty(value = "编号",required=false)
    private String code;

    @ApiModelProperty(value = "供应商主键",required=false)
    private String supplierId;

    @ApiModelProperty(value = "品种主键",required=false)
    private String categoryId;

    @ApiModelProperty(value = "当前编号",required=false)
    private String currentCode;

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

    @ApiModelProperty(value = "条码状态",hidden=true)
    private String status;

}
