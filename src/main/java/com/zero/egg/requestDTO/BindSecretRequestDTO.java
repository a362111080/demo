package com.zero.egg.requestDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName BindSecretRequestDTO
 * @Description 订货平台用户绑定秘钥RequestDTO
 * @Author lyming
 * @Date 2019/8/27 3:38 下午
 **/
@Data
public class BindSecretRequestDTO implements Serializable {

    private static final long serialVersionUID = -8392056955439412013L;

    /**
     * 秘钥
     */
    @NotBlank(message = "秘钥不能为空")
    private String secret;
}
