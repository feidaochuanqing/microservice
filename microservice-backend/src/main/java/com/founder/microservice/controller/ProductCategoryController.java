package com.founder.microservice.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.founder.microservice.domain.ProductCategory;
import com.founder.microservice.domain.User;
import com.founder.microservice.domain.result.ActionResult;
import com.founder.microservice.domain.result.ExceptionMessage;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.service.ProductCategoryService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/productCategory")
public class ProductCategoryController extends BaseController {
	@Autowired
	private ProductCategoryService productCategoryService;
	 
	@ApiOperation(value="查询所产品分类", notes="查询所有未删除的产品分类")
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public PageBean<ProductCategory> listAll() {
		PageBean<ProductCategory> pageBean = null;
		try {
			List<ProductCategory> categories = this.productCategoryService.listAll();
			pageBean = new PageBean<ProductCategory>(ExceptionMessage.SUCCESS);
			pageBean.setIsOk(true);
			pageBean.setRecordList(categories);
		}
		catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			pageBean = new PageBean<ProductCategory> (ExceptionMessage.FAILED);
		}
		return pageBean;
	}
	
	@ApiOperation(value="分页查询所有产品类别", notes="根据[类别名称]分页查询[未删除的产品分类")
	@ApiImplicitParam(name = "book", value = "图书详细实体", required = true, dataType = "Book")
	@RequestMapping(value = "/listByPage", method = RequestMethod.GET)
	public PageBean<ProductCategory> listByPage(HttpServletRequest request) {
		ProductCategory condition = new ProductCategory();
		if(null != request.getParameter("categoryName")) {
			condition.setCategoryName(request.getParameter("categoryName"));
		}else {
			condition.setCategoryName("");
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
		
		PageBean<ProductCategory> pageBean = null;
		try {
			pageBean = this.productCategoryService.listByCriteria(condition, pageNumber, pageSize);
			pageBean.setIsOk(true);
		}
		catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			pageBean = new PageBean<ProductCategory> (ExceptionMessage.FAILED);
		}
		return pageBean;
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public ActionResult insert(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setId(UUID.randomUUID().toString());
			productCategory.setCreateTime(new Date());
			productCategory.setCreateUser(User.getCurrentUser().getName());
			productCategory.setCategoryCode(request.getParameter("categoryCode"));
			productCategory.setCategoryName(request.getParameter("categoryName"));
			productCategory.setIsDeleted(false);
			productCategory.setLastModifyTime(new Date());
			productCategory.setLastModifyUser(User.getCurrentUser().getName());
			this.productCategoryService.insert(productCategory);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ActionResult update(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			String categoryId = request.getParameter("categoryId");
			String categoryName = request.getParameter("categoryName");
			String categoryCode = request.getParameter("categoryCode");
			
			ProductCategory productCategory = this.productCategoryService.getById(categoryId);
			productCategory.setCategoryName(categoryName);
			productCategory.setCategoryCode(categoryCode);
			productCategory.setLastModifyTime(new Date());
			productCategory.setLastModifyUser(User.getCurrentUser().getName());
			 
			this.productCategoryService.update(productCategory);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
	

	@RequestMapping(value = "/delete" , method = RequestMethod.GET)
	public ActionResult delete(HttpServletRequest request) {
		ActionResult actionResult = null;
		try {
			String categoryId = request.getParameter("categoryId");
			ProductCategory productCategory = this.productCategoryService.getById(categoryId);
			productCategory.setIsDeleted(true);
			productCategory.setDeleteTime(new Date());
			productCategory.setDeleteUser(User.getCurrentUser().getName());
			this.productCategoryService.update(productCategory);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
}
