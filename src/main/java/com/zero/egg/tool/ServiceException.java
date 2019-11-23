package com.zero.egg.tool;

import lombok.Getter;

/**
 * 自定义业务异常
 *
 * @ClassName ServiceException
 * @Author lyming
 * @Date 2018/11/6 17:15
 **/
@Getter
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 6405889068698121513L;
    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * @param errorCode
     * @param message
     */
    public ServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }


}
