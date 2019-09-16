package com.zero.egg.requestDTO;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ShipmentStaticRequestDTO
 * @Description TODO
 * @Author lyming
 * @Date 2019/9/16 3:33 下午
 **/
@Data
public class ShipmentStaticRequestDTO implements Serializable {

    private static final long serialVersionUID = 6258000803192036253L;

    @TableField(exist = false)
    @ApiModelProperty(value = "统计气势日期(汇总卸货数量使用yyyy-mm-dd 字符)",hidden=true)
    private String UnloadBeginTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "统计结束日期(汇总卸货数量使用yyyy-mm-dd 字符)",hidden=true)
    private String UnloadEndTime;

    private String companyId;

    private String shopId;
}
