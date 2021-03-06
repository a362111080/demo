package com.zero.egg.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zero.egg.annotation.LoginToken;
import com.zero.egg.api.ApiConstants;
import com.zero.egg.enums.ShopEnums;
import com.zero.egg.model.*;
import com.zero.egg.requestDTO.LoginUser;
import com.zero.egg.requestDTO.OrderGoodsRequestDTO;
import com.zero.egg.requestDTO.ShopRequest;
import com.zero.egg.responseDTO.OrderCategoryResponseDTO;
import com.zero.egg.service.IShopService;
import com.zero.egg.tool.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-13
 */
@RestController
@Api(value="店铺管理")
@RequestMapping("/shop")
public class ShopController {


	@Autowired
	private IShopService shopService;

	@Autowired
	private HttpServletRequest request;
	@LoginToken
	//@PassToken
	@ApiOperation(value="分页查询店铺")
	@RequestMapping(value="/list.data",method=RequestMethod.POST)
	public Message<IPage<Shop>> list(
			@RequestBody @ApiParam(required=false,name="shop",value="查询字段：关键词（名称 、编号）、状态") ShopRequest shop) {
		//ListResponse<Shop> response = new ListResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<IPage<Shop>> message = new Message<IPage<Shop>>();
		Page<Shop> page = new Page<>();
		page.setCurrent(shop.getCurrent());
		page.setSize(shop.getSize());
		QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dr", false);//查询未删除信息
		if (shop != null) {
			queryWrapper.like(StringUtils.isNotBlank(shop.getName()),"name", shop.getName())
			.like(StringUtils.isNotBlank(shop.getCode()),"code", shop.getCode())
			.eq(StringUtils.isNotBlank(shop.getStatus()), "status", shop.getStatus());
		}
		IPage<Shop> list = shopService.page(page, queryWrapper);
		message.setData(list);
		message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return message;

	}

	@LoginToken
	@ApiOperation(value="根据Id查询店铺")
	@RequestMapping(value="/get.data",method=RequestMethod.POST)
	public Message<Shop> getById(@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Shop> message = new Message<Shop>();
		Shop shop = shopService.getById(id);
		if (shop != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(shop);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="根据企业id查询店铺")
	@RequestMapping(value="/get-company.data",method=RequestMethod.POST)
	public Message<List<Shop>> getByCompanyId(@RequestParam @ApiParam(required=true,name="companyId",value="企业id") String companyId) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<List<Shop>> message = new Message<List<Shop>>();
		QueryWrapper<Shop> queryWrapper = new QueryWrapper<Shop>();
		queryWrapper.eq("company_id", companyId);
		queryWrapper.eq("dr", false);
		List<Shop> shopList = shopService.list(queryWrapper);
		if (shopList != null) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			message.setData(shopList);
		}else {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="新增店铺")
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public Message<Object> add(@RequestBody @ApiParam(required=true,name="shop",value="店铺信息:(全部必填)编号，名称，地址，联系电话，企业主键，pc端数量,boss端数量,员工端数量，设备端数量，业务员，实施员") Shop shop
			,HttpServletRequest request) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		message =shopService.save(shop, loginUser);
		return message;
	}

	@LoginToken
	@ApiOperation(value="根据id修改店铺信息")
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,@RequestBody  @ApiParam(required=true,name="shop",value="店铺信息：编号，名称，电话，地址，企业主键") Shop shop,HttpSession session) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		shop.setModifier(loginUser.getId());
		shop.setModifytime(new Date());
		if (shopService.updateById(shop)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="停用店铺")
	@RequestMapping(value="/stopshop.do",method=RequestMethod.POST)
	public Message<Object> edit(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		Shop shop = new Shop();
		shop.setStatus(ShopEnums.Status.Disable.index().toString());
		shop.setId(id);
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		shop.setModifier(loginUser.getId());
		shop.setModifytime(new Date());
		shop.setDr(true);
		if (shopService.updateById(shop)) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="根据id删除店铺信息")
	@RequestMapping(value="/del.do",method=RequestMethod.POST)
	public Message<Object> del(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="id",value="店铺id") String id) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		Shop shop = new Shop();
		shop.setDr(true);
		shop.setId(id);
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		shop.setModifier(loginUser.getId());
		shop.setModifytime(new Date());
		if (shopService.updateById(shop)) {//逻辑删除
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}else {
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="批量删除店铺信息")
	@RequestMapping(value="/batchdel.do",method=RequestMethod.POST)
	public Message<Object> batchDel(HttpServletRequest request,@RequestParam @ApiParam(required=true,name="ids",value="店铺ids,逗号拼接") String ids) {
		//BaseResponse<Object> response = new BaseResponse<>(ApiConstants.ResponseCode.EXECUTE_ERROR, ApiConstants.ResponseMsg.EXECUTE_ERROR);
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		List<String> idsList = StringTool.splitToList(ids, ",");
		if (idsList !=null) {
			List<Shop> shopList = new ArrayList<>();
			for (String id : idsList) {
				Shop shop = new Shop();
				shop.setDr(true);
				shop.setId(id);
				shop.setModifier(loginUser.getId());
				shop.setModifytime(new Date());
				shopList.add(shop);
			}
			if (shopService.updateBatchById(shopList)) {//逻辑删除
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
			}
		}else {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.FAILED);
		}

		return message;
	}


	//店铺秘钥
	@LoginToken
	@ApiOperation(value="新增店铺秘钥")
	@RequestMapping(value="/addsecret",method=RequestMethod.POST)
	public Message<Object> addsecret(@RequestBody  Shop shop) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (loginUser.getCompanyId()!=null) {
			String usecret = shop.getSecret();
			try {
				int strval = shopService.addsecret(shop, loginUser, usecret);
				if (strval > 0) {
					message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
					message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
				}
				else
				{
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("操作失败");
				}
			}
			catch (Exception e) {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				if (e instanceof ServiceException) {
					message.setMessage(e.getMessage());
				}
				message.setMessage((UtilConstants.ResponseMsg.FAILED));
			}
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}


	//店铺秘钥
	@LoginToken
	@ApiOperation(value="编辑店铺秘钥")
	@RequestMapping(value="/editsecret",method=RequestMethod.POST)
	public Message<Object> editsecret(@RequestBody  OrderSecret model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (loginUser.getCompanyId()!=null) {
			try {
				model.setShopid(loginUser.getShopId());
				model.setModifier(loginUser.getId());
				model.setModifytime(new Date());
				if (null != model.getIds()) {
					int cotVal=0;
					List<String> idsList = StringTool.splitToList(model.getIds(), ",");
					if (idsList !=null) {
						for (String id : idsList) {
							model.setId(id);
							cotVal+=shopService.editsecret(model);
						}
					}
					if (cotVal > 0) {
						message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
						message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
					} else {
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("操作失败");
					}
				}
				else {
					int strval = shopService.editsecret(model);
					if (strval > 0) {
						message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
						message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
					} else {
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("操作失败");
					}
				}
			}
			catch (Exception e) {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				if (e instanceof ServiceException) {
					message.setMessage(e.getMessage());
				}
				message.setMessage((UtilConstants.ResponseMsg.FAILED));
			}
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}


	//随机密钥
	@ApiOperation(value="查询店铺秘钥")
	@RequestMapping(value="/createsecret",method=RequestMethod.POST)
	public Message<Object> Createshopsecret(@RequestBody  ShopRequest shop) {
		Message ms = new Message();
		String usecret = generateShortUuid();
		ms.setData(usecret);
		ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
		ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		return ms;
	}




	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };

	/**
	 * 生成8位UUID
	 * @return 8位UUID
	 */
	public static String generateShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UuidUtil.get32UUID().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}


    //店铺秘钥
    @LoginToken
    @ApiOperation(value="查询店铺秘钥")
    @RequestMapping(value="/getshopsecret",method=RequestMethod.POST)
    public Message<Object> getshopsecret(@RequestBody  ShopRequest shop) {
        Message ms = new Message();
        //当前登录用户
        LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
        PageHelper.startPage(shop.getCurrent().intValue(), shop.getSize().intValue());
        shop.setCode(loginUser.getShopId());
        shop.setCompanyId(loginUser.getCompanyId());
        if (loginUser.getCompanyId()!=null) {
            List<OrderSecret> ListSecret=shopService.GetShopSecret(shop);
            PageInfo<OrderSecret> pageInfo = new PageInfo<>(ListSecret);
            ms.setData(pageInfo);
            ms.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
            ms.setMessage(UtilConstants.ResponseMsg.SUCCESS);
        }
        else
        {
            ms.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
            ms.setMessage("操作失败，无企业信息");
        }
        return ms;
    }



	//店铺秘钥
	@LoginToken
	@ApiOperation(value="新增店铺分类")
	@RequestMapping(value="/addordercategory",method=RequestMethod.POST)
	public Message<Object> addordercategory(@RequestBody OrderCategory model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		if (loginUser.getCompanyId()!=null) {
			try {
			    //判断类目名称是否重复
				model.setShopId(loginUser.getShopId());
                List<OrderCategory> res =shopService.getCategoryInfo(model);
                if (res.size()>0) {
                    message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                    message.setMessage("类目名称重复！");
                }
                else
                {
                    int strval = shopService.addordercategory(model, loginUser);
                    if (strval > 0) {
                        message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                        message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                    } else {
                        message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                        message.setMessage("操作失败");
                    }

                }
			}
			catch (Exception e) {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				if (e instanceof ServiceException) {
					message.setMessage(e.getMessage());
				}
				message.setMessage((UtilConstants.ResponseMsg.FAILED));
			}
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}



	@LoginToken
	@ApiOperation(value="修改店铺分类")
	@RequestMapping(value="/editordercategory",method=RequestMethod.POST)
	public Message<Object> editordercategory(@RequestBody OrderCategory model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setShopId(loginUser.getShopId());
		if (loginUser.getCompanyId()!=null) {
			try {
                //判断类目名称是否重复
                List<OrderCategory> res =shopService.getCategoryInfo(model);
                if (res.size()>0 && model.getDr()==null) {
                    if (res.size()==1 && res.get(0).getId().equals(model.getId())) {
                        int strval = shopService.editrdercategory(model);
                        if (strval > 0) {
                            message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
                            message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
                        } else {
                            message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
							message.setMessage("操作失败");
                        }
                    }
                    else
                    {
                        message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
                        message.setMessage("类目名称重复！");
                    }
                }
                else {
					List<OrderGoods> goodsList = shopService.GetOrderGoodsList(model);
					if (goodsList.size() > 0) {
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("类目下已存在商品，无法删除！");
					} else {
						int strval = shopService.editrdercategory(model);
						if (strval > 0) {
							message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
							message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
						} else {
							message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
							message.setMessage("操作失败");
						}
					}
				}
			}
			catch (Exception e) {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				if (e instanceof ServiceException) {
					message.setMessage(e.getMessage());
				}
				message.setMessage((UtilConstants.ResponseMsg.FAILED));
			}
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="查询店铺分类")
	@RequestMapping(value="/queryordercategory",method=RequestMethod.POST)
	public Message<Object> Queryordercategory(@RequestBody OrderCategory model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setShopId(loginUser.getShopId());
		model.setCompanyId(loginUser.getCompanyId());
		if (loginUser.getCompanyId()!=null) {
			List<OrderCategoryResponseDTO>  ResponseDTO=shopService.GetOrderCateGory(model);
			if (ResponseDTO.size()>0) {
				if ( model.getIsQueryChild()==true) {
					for (int m = 0; m < ResponseDTO.size(); m++) {
						if (ResponseDTO.get(m).getPicUrl()==null)
						{
							ResponseDTO.get(m).setPicUrl("zero.png");
						}
						model.setPid(ResponseDTO.get(m).getId());
						List<OrderCategory> child = shopService.GetOrderCateGoryChild(model);
						for (int n=0;n<child.size();n++) {
							if (child.get(n).getPicUrl() == null) {
								child.get(n).setPicUrl("zero.png");
							}
						}
						ResponseDTO.get(m).setOrderCategoryList(child);

					}
				}
			}
			message.setData(ResponseDTO);
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);

		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}


	@LoginToken
	@ApiOperation(value="新增店铺商品")
	@RequestMapping(value="/addordergood",method=RequestMethod.POST)
	public Message<Object> addordergood(@RequestBody  OrderGoods model) {
		Message<Object> message = new Message<Object>();
		ObjectMapper mapper = new ObjectMapper();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setShopId(loginUser.getShopId());
		if (loginUser.getCompanyId()!=null) {
			try {
				if (null!=model.getCategoryId() && null !=model.getShopId()) {
					int  checkval=0;
					if (model.getCategoryId().contains(","))
					{
						model.setCategoryId(model.getCategoryId().split(",")[1]);
					}
					//验证重名
					OrderGoods  checkModel=new OrderGoods();
					checkModel.setName(model.getName());
					checkModel.setCategoryId(model.getCategoryId());

					OrderGoods  checkname=shopService.getOrderGoodsForCheck(checkModel);
					if (null!=checkname)
					{
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("商品名称重复");
						checkval++;
					}
					checkModel.setName("");
					checkModel.setGoodsSn(model.getGoodsSn());
					OrderGoods  checkno=shopService.getOrderGoodsForCheck(checkModel);
					if (null!=checkno)
					{
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("商品编码重复");
						checkval++;
					}

					if (checkval<1) {
						int sort = shopService.GetOrderGoodsSort(model);
						model.setSortOrder(sort);
						int strval = shopService.addordergood(model, loginUser);
						if (strval > 0) {
							//增加商品明细
							if (model.getSepcificationList().size() > 0) {
								for (int m = 0; m < model.getSepcificationList().size(); m++) {
									//商品详情
									OrderGoodSpecification ogs = new OrderGoodSpecification();
									ogs.setId(UuidUtil.get32UUID());
									ogs.setGoodsId(model.getId());
									ogs.setSpecification(model.getSepcificationList().get(m).getSpecification());
									ogs.setValue(model.getSepcificationList().get(m).getValue());
									ogs.setPicUrl(model.getSepcificationList().get(m).getPicUrl());
									ogs.setPrice(model.getSepcificationList().get(m).getPrice());
									ogs.setModifier(loginUser.getId());
									ogs.setCreator(loginUser.getId());
									ogs.setCreatetime(new Date());
									ogs.setModifytime(new Date());
									ogs.setDr(false);
									shopService.addordergoodspec(ogs);
								}
							}
							message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
							message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
						} else {
							message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
							message.setMessage("操作失败");
						}
					}
				}
				else
				{
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("未选择商品类目信息！");
				}
			}
			catch (Exception e) {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				if (e instanceof ServiceException) {
					message.setMessage(e.getMessage());
				}
				message.setMessage((UtilConstants.ResponseMsg.FAILED));
			}
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}

	private String addThumbnail(LoginUser loginUser, ImageHolder imageHolder) {
		String dest = PathUtil.getShopImagePath(loginUser.getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnal(imageHolder, dest);
		return  thumbnailAddr;
		//TODO 可以传商品信息,将thumbnailAddr赋值给商品信息
	}


	@LoginToken
	@ApiOperation(value="修改店铺商品")
	@RequestMapping(value="/editordergood",method=RequestMethod.POST)
	public Message<Object> editordergood(@RequestBody OrderGoods model) {
		Message<Object> message = new Message<Object>();
		ObjectMapper mapper = new ObjectMapper();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setShopId(loginUser.getShopId());
		if (loginUser.getCompanyId()!=null) {
			try {
				if (null!=model.getCategoryId() && null !=model.getShopId()) {
					if (model.getCategoryId().contains(","))
					{
						model.setCategoryId(model.getCategoryId().split(",")[1]);
					}
					//验证重名
					int checkval=0;
					OrderGoods  checkModel=new OrderGoods();
					checkModel.setId(model.getId());
					checkModel.setName(model.getName());
					checkModel.setCategoryId(model.getCategoryId());
					OrderGoods  checkname=shopService.getOrderGoodsForCheck(checkModel);
					if (null!=checkname)
					{
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("商品名称重复");
						checkval++;
					}
					checkModel.setName("");
					checkModel.setGoodsSn(model.getGoodsSn());
					OrderGoods  checkno=shopService.getOrderGoodsForCheck(checkModel);
					if (null!=checkno)
					{
						message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
						message.setMessage("商品编码重复");
						checkval++;
					}
					if (checkval<1) {
						int strval = shopService.editordergood(model, loginUser);
						if (strval > 0) {
							//增加商品明细
							//先删除再增加
							shopService.updateGoodSepcification(model);
							if (null !=model.getSepcificationList()) {

								if (model.getSepcificationList().size() > 0) {
									for (int m = 0; m < model.getSepcificationList().size(); m++) {
										//商品详情
										OrderGoodSpecification ogs = new OrderGoodSpecification();
										if (null != model.getSepcificationList().get(m).getId()) {
											//修改
											ogs.setId(model.getSepcificationList().get(m).getId());
											ogs.setGoodsId(model.getId());
											ogs.setSpecification(model.getSepcificationList().get(m).getSpecification());
											ogs.setValue(model.getSepcificationList().get(m).getValue());
											ogs.setPicUrl(model.getSepcificationList().get(m).getPicUrl());
											ogs.setPrice(model.getSepcificationList().get(m).getPrice());
											ogs.setModifier(loginUser.getId());
											ogs.setModifytime(new Date());
											ogs.setDr(false);
											shopService.editordergoodspec(ogs);
										} else {
											//新增
											ogs.setId(UuidUtil.get32UUID());
											ogs.setGoodsId(model.getId());
											ogs.setSpecification(model.getSepcificationList().get(m).getSpecification());
											ogs.setValue(model.getSepcificationList().get(m).getValue());
											ogs.setPicUrl(model.getSepcificationList().get(m).getPicUrl());
											ogs.setPrice(model.getSepcificationList().get(m).getPrice());
											ogs.setModifier(loginUser.getId());
											ogs.setCreator(loginUser.getId());
											ogs.setCreatetime(new Date());
											ogs.setModifytime(new Date());
											ogs.setDr(false);
											shopService.addordergoodspec(ogs);
										}
									}
								}
							}
							message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
							message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
						} else {
							message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
							message.setMessage("操作失败");
						}
					}
				}
				else
				{
					message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
					message.setMessage("未选择商品类目信息！");
				}
			}
			catch (Exception e) {
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				if (e instanceof ServiceException) {
					message.setMessage(e.getMessage());
				}
				message.setMessage((UtilConstants.ResponseMsg.FAILED));
			}
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}


	@LoginToken
	@ApiOperation(value="删除店铺商品")
	@RequestMapping(value="/delordergood",method=RequestMethod.POST)
	public Message<Object> delordergood(@RequestBody OrderGoods model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setDr(true);
		int strVal=shopService.editordergood(model,loginUser);
		if (strVal>0) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("删除失败！");
		}
		return message;
	}


	@LoginToken
	@ApiOperation(value="查询店铺商品")
	@RequestMapping(value="/queryordergoods",method=RequestMethod.POST)
	public Message<Object> queryordergoods(@RequestBody OrderGoodsRequestDTO model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		PageHelper.startPage(model.getCurrent().intValue(), model.getSize().intValue());
		model.setShopId(loginUser.getShopId());
		model.setCompanyId(loginUser.getCompanyId());
		if (loginUser.getCompanyId()!=null) {
			List<OrderGoods> ResponseDTO = shopService.GetOrderGoods(model);
				if (ResponseDTO.size() > 0) {
					for (int m = 0; m < ResponseDTO.size(); m++) {
						List<OrderGoodSpecification> spec = shopService.GetOrderGoodSpecList(ResponseDTO.get(m));
						ResponseDTO.get(m).setSepcificationList(spec);
						ResponseDTO.get(m).setCategoryId(ResponseDTO.get(m).getPid()+","+ResponseDTO.get(m).getCategoryId());
					}
				}
				PageInfo<OrderGoods> pageInfo = new PageInfo<>(ResponseDTO);
				message.setData(pageInfo);
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);

		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}

	@LoginToken
	@ApiOperation(value="查询店铺有效类目")
	@RequestMapping(value="/queryshopcategory",method=RequestMethod.POST)
	public Message<Object> queryshopcategory(@RequestBody OrderGoodsRequestDTO model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setShopId(loginUser.getShopId());
		model.setCompanyId(loginUser.getCompanyId());
		if (loginUser.getCompanyId()!=null) {
			//判断是否存在有效类目选择
			List<OrderCategory> child=shopService.GetOrderCateGoryList(model);
			if (child.size()>0) {
				message.setData(child.get(0));
				message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
				message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
			}
			else
			{
				message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
				message.setMessage("未找到可用的二级类目，请完善类目后再操作");
			}
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("操作失败，无企业信息");
		}
		return message;
	}





	@LoginToken
	@ApiOperation(value="删除店铺商品规格")
	@RequestMapping(value="/delordergoodspec",method=RequestMethod.POST)
	public Message<Object> delordergoodspec(@RequestBody OrderGoodSpecification model) {
		Message<Object> message = new Message<Object>();
		//当前登录用户
		LoginUser loginUser = (LoginUser) request.getAttribute(ApiConstants.LOGIN_USER);
		model.setDr(true);
		int strVal=shopService.editordergoodspec(model);
		if (strVal>0) {
			message.setState(UtilConstants.ResponseCode.SUCCESS_HEAD);
			message.setMessage(UtilConstants.ResponseMsg.SUCCESS);
		}
		else
		{
			message.setState(UtilConstants.ResponseCode.EXCEPTION_HEAD);
			message.setMessage("删除失败！");
		}
		return message;
	}

}
