package com.zero.egg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zero.egg.requestDTO.FinancialAccountRequestDTO;
import com.zero.egg.service.FinancialAccountService;
import com.zero.egg.tool.Message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 财务账单
 * 
 * @author JC
 *
 */
@Api(value="财务账单")
@RestController
@RequestMapping("/financial")
public class FinancialAccountController {
	@Autowired
	private FinancialAccountService service;

	@ApiOperation(value="查询财务账单")
	@RequestMapping(value = "/getlist", method = RequestMethod.POST)
	public Message getList(@RequestBody FinancialAccountRequestDTO dto) {
		Message message = service.getList(dto);
		return message;
	}
	
	@ApiOperation(value="销账")
	@RequestMapping(value = "/cancelaccount/{accountId}", method = RequestMethod.GET)
	public Message cancelAccount(@PathVariable String accountId) {
		Message message = service.cancelAccount(accountId);
		return message;
	}
}
