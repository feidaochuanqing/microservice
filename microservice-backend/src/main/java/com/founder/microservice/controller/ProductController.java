package com.founder.microservice.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.founder.microservice.domain.Product;
import com.founder.microservice.domain.ProductCategory;
import com.founder.microservice.domain.User;
import com.founder.microservice.domain.result.ActionResult;
import com.founder.microservice.domain.result.ExceptionMessage;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.domain.result.PageParam;
import com.founder.microservice.service.ProductCategoryService;
import com.founder.microservice.service.ProductService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 产品控制器
 * @author: lizhch
 * @date: 2017年12月4日 下午4:52:54  
 * @version V1.0
 */
@RestController
public class ProductController extends BaseController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	 
	
	@ApiOperation(value = "分页查询所有产品", notes = "根据[名称]分页查询[未删除的产品,查询所有参数写'all'")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "productName", dataType = "String", required = true, value = "产品名称",defaultValue="苹果"),
			@ApiImplicitParam(paramType = "body", name = "pageParam", dataType = "PageParam", value = "分页信息", required = true) })
	@RequestMapping(value = "/product/listByPage/{productName}", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
	public PageBean<Product> listProductsByPage(@PathVariable("productName") String productName, @RequestBody PageParam pageParam) {
		Product condition = new Product();
		if(productName != null) {
			if(productName.equals("all")) {
				condition.setProductName("");
			} else {
				condition.setProductName(productName);
			}
		}else {
			condition.setProductName("");
		}
		
		int pageSize = 10;
		int pageNumber = 1;

		if (pageParam != null) {
			pageSize = pageParam.getNumPerPage();
			pageNumber = pageParam.getPageNum();
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
	
	@ApiOperation(value = "查询", notes = "根据产品分类进行产品查询")
	@ApiImplicitParam(paramType = "path", name = "categoryId", dataType = "String", required = true, value = "类别主键")
	@RequestMapping(value = "/product/listByCategory/{categoryId}",method = RequestMethod.GET)
	public PageBean<Product> listAll(@PathParam("categoryId") String categoryId) {
		PageBean<Product> pageBean = null;
		try {
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
	
	@ApiOperation(value = "查询", notes = "查询单个产品")
	@RequestMapping(value = "/product/{productId}",method = RequestMethod.GET)
	public PageBean<Product> get(@PathVariable("productId") String productId) {
		PageBean<Product> pagebean = new PageBean<>();
		try {
			Product product = this.productService.getById(productId);
			pagebean.setIsOk(true);
			pagebean.setRecordList(Arrays.asList(product));
		}catch(Exception ex) {
			pagebean.setIsOk(false);
			this.logger.error(ex.getMessage(), ex);
		}
		return pagebean;
	}
	
	@ApiOperation(value = "新建", notes = "创建产品")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "path", name = "productCategory", dataType = "String", value = "产品类别主键", required = true),
		@ApiImplicitParam(paramType = "path", name = "productCode", dataType = "String", value = "产品编码", required = true),
		@ApiImplicitParam(paramType = "path", name = "productName", dataType = "String", value = "产品名称", required = true),
	})
	@RequestMapping(value = "/product/{categoryId}/{productCode}/{productName}",method = RequestMethod.PUT)
	public ActionResult insertProduct(
			@PathVariable("categoryId") String categoryId,
			@PathVariable("productCode") String productCode,
			@PathVariable("productName") String productName
			) {
		ActionResult actionResult = null;
		try {
			Product product = new Product();
			product.setId(UUID.randomUUID().toString());
			product.setCreateTime(new Date());
			product.setCreatUser(User.getCurrentUser().getName());
			product.setProductCode(productCode);
			product.setProductName(productName);
			product.setIsDeleted(false);
			product.setLastModifyTime(new Date());
			product.setLastModifyUser(User.getCurrentUser().getName());
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
	
	@ApiOperation(value = "更新", notes = "更新产品")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "path", name = "productId", dataType = "String", value = "产品主键", required = true),
		@ApiImplicitParam(paramType = "path", name = "productCategory", dataType = "String", value = "产品分类主键", required = true),
		@ApiImplicitParam(paramType = "path", name = "productCode", dataType = "String", value = "产品编号", required = true),
		@ApiImplicitParam(paramType = "path", name = "productName", dataType = "String", value = "产品名称", required = true),
	})
	@RequestMapping(value = "/product/{productId}/{categoryId}/{productCode}/{productName}",method = RequestMethod.POST)
	public ActionResult updateProduct(
			@PathVariable("productId") String productId,
			@PathVariable("categoryId") String categoryId,
			@PathVariable("productCode") String productCode,
			@PathVariable("productName") String productName) {
		ActionResult actionResult = null;
		try {
			Product product = this.productService.getById(productId);
			if(product != null) {
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
			}
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		}catch(Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
	

	@ApiOperation(value = "删除", notes = "删除产品")
	@ApiImplicitParam(paramType = "path", name = "productId", dataType = "String", required = true, value = "产品主键")
	@RequestMapping(value="/product/{productId}",method = RequestMethod.DELETE)
	public ActionResult deleteProduct(@PathVariable("productId") String productId) {
		ActionResult actionResult = null;
		try {
			Product product = this.productService.getById(productId);
			if(product != null) {
				product.setIsDeleted(true);
				product.setDeleteTime(new Date());
				product.setDeleteUser(User.getCurrentUser().getName());
				this.productService.update(product);
			}
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
