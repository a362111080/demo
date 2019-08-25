package com.zero.egg.requestDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName OrderAddressDTO
 * @Description TODO
 * @Author lyming
 * @Date 2019-08-21 10:37
 **/
@Data
public class OrderAddressDTO implements Serializable {

    private static final long serialVersionUID = 4335370608043917698L;

    /**
     * 收货人名称
     */
    @ApiModelProperty(value="收货人名称")
    private String name;

    /**
     * 订货平台用户表的用户ID
     */
    @ApiModelProperty(value="订货平台用户表的用户ID")
    private String userId;

    /**
     * 行政区域表的市ID
     */
    @ApiModelProperty(value="行政区域表的市ID")
    @NotNull(message ="地区不能为空")
    private String cityId;

    /**
     * 详细收货地址
     */
    @ApiModelProperty(value="详细收货地址")
    @NotBlank(message = "详细地址不能为空")
    private String addressDetail;

    /**
     * 地区编码
     */
    @ApiModelProperty(value="地区编码")
    private String areaCode;

    /**
     * 手机号码
     */
    @ApiModelProperty(value="手机号码")
    private String tel;

    /**
     * 是否默认地址
     */
    @ApiModelProperty(value="是否默认地址")
    private Boolean isDefault = false;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createtime;

    /**
     * 修改人
     */
    @ApiModelProperty(value="修改人")
    private String modifier;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date modifytime;
    /**
     * 逻辑删除
     */
    @ApiModelProperty(value="逻辑删除")
    private Boolean dr = false;
}
