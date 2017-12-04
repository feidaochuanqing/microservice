package com.founder.microservice.controller;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.founder.microservice.domain.Order;
import com.founder.microservice.domain.Product;
import com.founder.microservice.domain.User;
import com.founder.microservice.domain.result.ActionResult;
import com.founder.microservice.domain.result.ExceptionMessage;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.service.OrderService;
import com.founder.microservice.service.ProductService;

/**
 * 订单控制器类
 * @author: lizhch
 * @date: 2017年12月4日 下午4:52:18  
 * @version V1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/insert")
	public ActionResult insert(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			String count = request.getParameter("count");
			String productId = request.getParameter("productId");
			String address = request.getParameter("address");
			
			Product product = productService.getById(productId);
			Order order = new Order();
			order.setId(UUID.randomUUID().toString());
			order.setCreateTime(new Date());
			order.setCreateUser(User.getCurrentUser().getName());
			order.setAddress(address);
			order.setCount(Integer.parseInt(count));
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
	
	
	
	
	@RequestMapping(value = "/listByPage",method = RequestMethod.GET)
	public PageBean<Order> listByPage(HttpServletRequest request) {
		String productName = request.getParameter("productName") ;
		if(productName == null) {
			productName = "";
		}
		
		int pageSize = 10;
		int pageNumber = 1;
		
		String strPageSize = request.getParameter("pageSize");
		String strPageNumber = request.getParameter("pageNumber");
		
		if(null != strPageSize) {
			pageSize = Integer.parseInt(strPageSize);
		}
		if(null != strPageNumber) {
			pageNumber = Integer.parseInt(strPageNumber);
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
	
	@RequestMapping(value = "/update",method = RequestMethod.GET)
	public ActionResult update(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			String orderId = request.getParameter("orderId");
			String count = request.getParameter("count");
			String productId = request.getParameter("productId");
			String address = request.getParameter("address");
			
			Product product = this.productService.getById(productId);
			
			Order order = orderService.getById(orderId);
			order.setAddress(address);
			order.setCount(Integer.parseInt(count));
			order.setLastModifyTime(new Date());
			order.setLastModifyUser(User.getCurrentUser().getName());
			if(product != null) {
				order.setProduct(product);
			}
			this.orderService.update(order);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
	

	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	public ActionResult delete(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			String orderId = request.getParameter("orderId");
			Order order = orderService.getById(orderId);
			order.setIsDeleted(true);
			order.setDeleteTime(new Date());
			order.setDeleteUser(User.getCurrentUser().getName());
			this.orderService.update(order);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
}
