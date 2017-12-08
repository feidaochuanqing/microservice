package com.founder.microservice.controller;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.founder.microservice.domain.Order;
import com.founder.microservice.domain.Product;
import com.founder.microservice.domain.User;
import com.founder.microservice.domain.result.ActionResult;
import com.founder.microservice.domain.result.ExceptionMessage;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.domain.result.PageParam;
import com.founder.microservice.service.OrderService;
import com.founder.microservice.service.ProductService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 订单控制器类
 * @author: lizhch
 * @date: 2017年12月4日 下午4:52:18  
 * @version V1.0
 */
@RestController
public class OrderController extends BaseController {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@ApiOperation(value = "新建", notes = "创建订单")
	@ApiImplicitParams(
			{
			@ApiImplicitParam(paramType = "query", name = "productId", dataType = "String", value = "产品主键", required = true),
			@ApiImplicitParam(paramType = "body", name = "order", dataType = "Order", value = "订单信息", required = true)
			})
	@RequestMapping(value = "/order/{productId}", method=RequestMethod.POST)
	public ActionResult insert(@PathVariable("productId") String productId, @RequestBody Order order) {
		ActionResult actionResult = null;
		try {
			Product product = productService.getById(productId);
			order.setId(UUID.randomUUID().toString());
			order.setCreateTime(new Date());
			order.setCreateUser(User.getCurrentUser().getName());
			order.setIsDeleted(false);
			order.setLastModifyTime(new Date());
			order.setLastModifyUser(User.getCurrentUser().getName());
			order.setProduct(product);
			this.orderService.insert(order);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
	
	@ApiOperation(value = "分页查询所有订单", notes = "根据[类别名称]分页查询[未删除的产品分类,查询所有参数写'all'")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "productName", dataType = "String", required = true, value = "产品名称", defaultValue = "all"),
			@ApiImplicitParam(paramType = "body", name = "pageParam", dataType = "PageParam", value = "分页信息", required = true) })
	@RequestMapping(value = "/order/listByPage/{productName}", method = RequestMethod.POST)
	public PageBean<Order> listByPage(@PathVariable("productName") String productName,@RequestBody PageParam pageParam) {
		if(productName == null) {
			productName = "";
		} else if(productName.equals("all")) {
			productName = "";
		}
		
		int pageSize = 10;
		int pageNumber = 1;
		if (pageParam != null) {
			pageSize = pageParam.getNumPerPage();
			pageNumber = pageParam.getPageNum();
		}
		
		PageBean<Order> pageBean = null;
		try {
			pageBean = this.orderService.listByProductName(productName, pageNumber, pageSize);
			pageBean.setIsOk(true);
		}
		catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			pageBean = new PageBean<Order> (ExceptionMessage.FAILED);
		}
		return pageBean;
	}
	
	@ApiOperation(value = "更新", notes = "更新订单")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "类别主键"),
			@ApiImplicitParam(paramType = "path", name = "productId", dataType = "String", required = true, value = "类别主键"),
			@ApiImplicitParam(paramType = "body", name = "order", dataType = "ProductCategory", value = "产品分类信息", required = true) })
	@RequestMapping(value = "/order/{orderId}/{productId}", method = RequestMethod.PUT)
	public ActionResult update(@PathVariable("orderId") String orderId, @PathVariable("productId") String productId,@RequestBody Order order2) {
		ActionResult actionResult = null;
		try {
			Product product = this.productService.getById(productId);
				Order order = orderService.getById(orderId);
				if(order != null) {
				order.setAddress(order2.getAddress());
				order.setCount(order2.getCount());
				order.setLastModifyTime(new Date());
				order.setLastModifyUser(User.getCurrentUser().getName());
				if(product != null) {
					order.setProduct(product);
				}
				this.orderService.update(order);
			}
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
	

	@ApiOperation(value = "删除", notes = "删除订单")
	@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单主键")
	@RequestMapping(value = "/order/{orderId}", method = RequestMethod.DELETE)
	public ActionResult delete(@PathVariable("orderId") String orderId) {
		ActionResult actionResult = null;
		try {
			Order order = orderService.getById(orderId);
			if(order != null) {
				order.setIsDeleted(true);
				order.setDeleteTime(new Date());
				order.setDeleteUser(User.getCurrentUser().getName());
				this.orderService.update(order);
			}
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
}
