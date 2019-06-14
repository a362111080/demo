package com.zero.egg.controller;

import com.zero.egg.tool.AuthenticateException;
import com.zero.egg.tool.Message;
import com.zero.egg.tool.ServiceException;
import com.zero.egg.tool.UtilConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Rest 异常处理器
 * 
 * @author anthony.guo
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

	@ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = ServiceException.class)
	public Message handleSaveException(ServiceException e) {
		log.error(e.getMessage(), e);
		return new Message(UtilConstants.ResponseCode.EXCEPTION_HEAD, e.getMessage());
    }
	
	@ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = AuthenticateException.class)
	public Message handleAuthenticateException(AuthenticateException e) {
		log.error(e.getMessage(), e);
		return new Message(e.getCode(), e.getMessage());
    }

}
