package com.zero.egg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zero.egg.requestDTO.OutGoodsDTO;
import com.zero.egg.service.OutGoodsService;
import com.zero.egg.tool.Message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 出货管理
 * 
 * @author JC
 *
 */

@Api(value="出货管理")
@RestController
@RequestMapping("/outgoods")
public class OutGoodsController {

	@Autowired
	private OutGoodsService service;
	
	@ApiOperation(value="出货管理")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Message save(@RequestBody OutGoodsDTO dto) {
		Message message = service.outGoods(dto);
		return message;
	}
}
