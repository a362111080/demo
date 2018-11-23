package com.zero.egg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zero.egg.requestDTO.IncomingGoodsDTO;
import com.zero.egg.service.IncomingGoodsService;
import com.zero.egg.tool.Message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 卸货入库
 * 
 * @author JC
 *
 */

@Api(value = "卸货管理")
@RestController
@RequestMapping("/incominggoods")
public class IncomingGoodsController {

	@Autowired
	private IncomingGoodsService service;

	@ApiOperation(value="卸货入库")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Message save(@RequestBody IncomingGoodsDTO dto) {
		Message message = service.saveGoods(dto);
		return message;
	}
}
