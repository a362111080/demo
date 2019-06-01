package com.zero.egg.responseDTO;

import com.zero.egg.model.Shop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CompanyinfoResponseDto {


    @ApiModelProperty(value = "企业用户id")
    private String id;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "企业id")
    private String companyId;

    @ApiModelProperty(value = "账号")
    private String loginname;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "有效期至")
    private String endtime;

    @ApiModelProperty(value = "开始时间")
    private String begintime;

    @ApiModelProperty(value = "开通时间")
    private String createtime;

    @ApiModelProperty(value = "最近使用时间")
    private String lastusetime;

    @ApiModelProperty(value = "pc端数量",required=false)
    private Integer pcClientCount;

    @ApiModelProperty(value = "boss端数量",required=false)
    private Integer bossClientCount;

    @ApiModelProperty(value = "员工端数量",required=false)
    private Integer staffClientCount;

    @ApiModelProperty(value = "设备端数量",required=false)
    private Integer deviceClientCount;


    @ApiModelProperty(value = "企业店铺信息",hidden=true)
    private List<Shop> shopList;

}
