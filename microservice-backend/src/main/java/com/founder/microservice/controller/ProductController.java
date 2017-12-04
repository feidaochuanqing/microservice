package com.founder.microservice.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.founder.microservice.domain.Product;
import com.founder.microservice.domain.ProductCategory;
import com.founder.microservice.domain.User;
import com.founder.microservice.domain.result.ActionResult;
import com.founder.microservice.domain.result.ExceptionMessage;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.service.ProductCategoryService;
import com.founder.microservice.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	 
	@RequestMapping(value = "/listProductsByPage" ,method = RequestMethod.GET)
	public PageBean<Product> listProductsByPage(HttpServletRequest request) {
		Product condition = new Product();
		if(null != request.getParameter("productName")) {
			condition.setProductName(request.getParameter("productName"));
		}else {
			condition.setProductName("");
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
		
		PageBean<Product> pageBean = null;
		try {
			pageBean = this.productService.listByCriteria(condition, pageNumber, pageSize);
			pageBean.setIsOk(true);
		}
		catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			pageBean = new PageBean<Product> (ExceptionMessage.FAILED);
		}
		return pageBean;
	}
	
	@RequestMapping(value = "/listAll",method = RequestMethod.GET)
	public PageBean<Product> listAll(HttpServletRequest request) {
		PageBean<Product> pageBean = null;
		try {
			String categoryId = request.getParameter("categoryId"); 
			List<Product> products = null;
			if(null == categoryId || "".equals(categoryId)) {
				products = this.productService.listAll();
			}else {
				products = this.productService.listByCategoryId(categoryId);
			}
			pageBean = new PageBean<Product>(ExceptionMessage.SUCCESS);
			pageBean.setRecordList(products);
		}
		catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			pageBean = new PageBean<Product> (ExceptionMessage.FAILED);
		}
		return pageBean;
	}
	
	@RequestMapping(value = "/get",method = RequestMethod.GET)
	public PageBean<Product> get(HttpServletRequest request) {
		PageBean<Product> pagebean = new PageBean<>();
		try {
			Product product = this.productService.getById(request.getParameter("productId"));
			pagebean.setIsOk(true);
			pagebean.setRecordList(Arrays.asList(product));
		}catch(Exception ex) {
			pagebean.setIsOk(false);
			this.logger.error(ex.getMessage(), ex);
		}
		return pagebean;
	}
	
	@RequestMapping(value = "/insert",method = RequestMethod.GET)
	public ActionResult insertProduct(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			Product product = new Product();
			product.setId(UUID.randomUUID().toString());
			product.setCreateTime(new Date());
			product.setCreatUser(User.getCurrentUser().getName());
			product.setProductCode(request.getParameter("productCode"));
			product.setProductName(request.getParameter("productName"));
			product.setIsDeleted(false);
			product.setLastModifyTime(new Date());
			product.setLastModifyUser(User.getCurrentUser().getName());
			String categoryId = request.getParameter("productCategory");
			if(null != categoryId && ""!= categoryId.trim()) {
				ProductCategory category = this.productCategoryService.getById(categoryId);
				if(category != null && category.getId() != null) {
					product.setProductCategory(category);
				}
			}
			this.productService.insert(product);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
			actionResult.setIsOk(true);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
			actionResult.setIsOk(false);
		}
		return actionResult;
	}
	
	@RequestMapping(value="/update",method = RequestMethod.GET)
	public ActionResult updateProduct(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			String productId = request.getParameter("productId");
			String productName = request.getParameter("productName");
			String productCode = request.getParameter("productCode");
			String categoryId = request.getParameter("productCategory");
			
			Product product = this.productService.getById(productId);
			product.setProductCode(productCode);
			product.setProductName(productName);
			product.setLastModifyTime(new Date());
			product.setLastModifyUser(User.getCurrentUser().getName());
			
			if(null != categoryId && ""!= categoryId.trim()) {
				ProductCategory category = this.productCategoryService.getById(categoryId);
				if(category != null && category.getId() != null) {
					product.setProductCategory(category);
				}
			}
			this.productService.update(product);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
	

	@RequestMapping(value="/delete",method = RequestMethod.GET)
	public ActionResult deleteProduct(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			String productId = request.getParameter("productId");
			Product product = this.productService.getById(productId);
			product.setIsDeleted(true);
			product.setDeleteTime(new Date());
			product.setDeleteUser(User.getCurrentUser().getName());
			this.productService.update(product);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
			actionResult.setIsOk(true);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
			actionResult.setIsOk(false);
		}
		return actionResult;
	}
}
