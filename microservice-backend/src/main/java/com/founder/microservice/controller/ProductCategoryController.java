package com.founder.microservice.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.founder.microservice.domain.ProductCategory;
import com.founder.microservice.domain.User;
import com.founder.microservice.domain.result.ActionResult;
import com.founder.microservice.domain.result.ExceptionMessage;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.domain.result.PageParam;
import com.founder.microservice.service.ProductCategoryService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 产品类别控制器
 * 
 * @author: lizhch
 * @date: 2017年12月4日 下午4:52:38
 * @version V1.0
 */
@RestController
public class ProductCategoryController extends BaseController {

	@Autowired
	private ProductCategoryService productCategoryService;

	@ApiOperation(value = "查询所产品分类", notes = "查询所有未删除的产品分类")
	@RequestMapping(value = "/productCategory/listAll", method = RequestMethod.GET)
	public PageBean<ProductCategory> listAll() {
		PageBean<ProductCategory> pageBean = null;
		try {
			List<ProductCategory> categories = this.productCategoryService.listAll();
			pageBean = new PageBean<ProductCategory>(ExceptionMessage.SUCCESS);
			pageBean.setIsOk(true);
			pageBean.setRecordList(categories);
		} catch (Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			pageBean = new PageBean<ProductCategory>(ExceptionMessage.FAILED);
		}
		return pageBean;
	}

	@ApiOperation(value = "分页查询所有产品类别", notes = "根据[类别名称]分页查询[未删除的产品分类,查询所有参数写'all'")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "categoryName", dataType = "String", required = true, value = "类别名称", defaultValue = "all"),
			@ApiImplicitParam(paramType = "body", name = "pageParam", dataType = "PageParam", value = "分页信息", required = true) })
	@RequestMapping(value = "/productCategory/listByPage/{categoryName}", method = RequestMethod.POST , consumes="application/json")
	public PageBean<ProductCategory> listByPage(@PathVariable("categoryName") String categoryName,
			@RequestBody PageParam pageParam) {
		ProductCategory condition = new ProductCategory();
		if (categoryName != null) {
			condition.setCategoryName(categoryName);
			if (categoryName.equals("all")) {
				condition.setCategoryName("");
			}
		} else {
			condition.setCategoryName("");
		}

		int pageSize = 10;
		int pageNumber = 1;

		if (pageParam != null) {
			pageSize = pageParam.getNumPerPage();
			pageNumber = pageParam.getPageNum();
		}
		System.out.println(categoryName);
		PageBean<ProductCategory> pageBean = null;
		try {
			pageBean = this.productCategoryService.listByCriteria(condition, pageNumber, pageSize);
			pageBean.setIsOk(true);
		} catch (Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			pageBean = new PageBean<ProductCategory>(ExceptionMessage.FAILED);
		}
		return pageBean;
	}

	@ApiOperation(value = "新建", notes = "创建产品分类")
	@ApiImplicitParam(paramType = "body", name = "category", dataType = "ProductCategory", value = "产品分类信息", required = true)
	@RequestMapping(value = "/productCategory/", method = RequestMethod.POST)
	public ActionResult insert(@RequestBody ProductCategory category) {
		ActionResult actionResult = null;
		try {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setId(UUID.randomUUID().toString());
			productCategory.setCreateTime(new Date());
			productCategory.setCreateUser(User.getCurrentUser().getName());
			productCategory.setCategoryCode(category.getCategoryCode());
			productCategory.setCategoryName(category.getCategoryName());
			productCategory.setIsDeleted(false);
			productCategory.setLastModifyTime(new Date());
			productCategory.setLastModifyUser(User.getCurrentUser().getName());
			this.productCategoryService.insert(productCategory);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		} catch (Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}

	@ApiOperation(value = "更新", notes = "更新产品分类")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "id", dataType = "String", required = true, value = "类别主键"),
			@ApiImplicitParam(paramType = "body", name = "category", dataType = "ProductCategory", value = "产品分类信息", required = true) })
	@RequestMapping(value = "/productCategory/", method = RequestMethod.PUT)
	public ActionResult update(@RequestBody ProductCategory category) {
		ActionResult actionResult = null;
		try {
			ProductCategory productCategory = this.productCategoryService.getById(category.getId());

			String categoryName = category.getCategoryName();
			String categoryCode = category.getCategoryCode();

			productCategory.setCategoryName(categoryName);
			productCategory.setCategoryCode(categoryCode);
			productCategory.setLastModifyTime(new Date());
			productCategory.setLastModifyUser(User.getCurrentUser().getName());

			this.productCategoryService.update(productCategory);
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		} catch (Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}

	@ApiOperation(value = "删除", notes = "删除产品分类")
	@ApiImplicitParam(paramType = "path", name = "id", dataType = "String", required = true, value = "类别主键")
	@RequestMapping(value = "/productCategory/{categoryId}", method = RequestMethod.DELETE)
	public ActionResult delete(@PathVariable("categoryId") String categoryId) {
		ActionResult actionResult = null;
		try {
			ProductCategory productCategory = this.productCategoryService.getById(categoryId);
			if(productCategory != null) {
				productCategory.setIsDeleted(true);
				productCategory.setDeleteTime(new Date());
				productCategory.setDeleteUser(User.getCurrentUser().getName());
				this.productCategoryService.update(productCategory);
			}
			actionResult = new ActionResult(ExceptionMessage.SUCCESS);
		} catch (Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			actionResult = new ActionResult(ExceptionMessage.FAILED);
		}
		return actionResult;
	}
}
