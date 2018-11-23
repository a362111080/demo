package com.zero.egg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zero.egg.requestDTO.FinancialAccountRequestDTO;
import com.zero.egg.service.FinancialAccountDetailService;
import com.zero.egg.tool.Message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 财务账单明细
 * 
 * @author JC
 *
 */
@Api(value="财务账单明细")
@RestController
@RequestMapping("/financialdetail")
public class FinancialAccountDetailController {
	@Autowired
	private FinancialAccountDetailService service;
	
	@ApiOperation(value="查询财务账单明细")
	@RequestMapping(value = "/getlistbyaccountid/{id}", method = RequestMethod.GET)
	public Message getListByAccountId(@PathVariable String id) {
		Message message = service.getListByAccountId(id);
		return message;
	}
}
