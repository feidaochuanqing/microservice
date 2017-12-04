package com.founder.microservice.service;

import java.util.List;

import com.founder.microservice.domain.ProductCategory;
import com.founder.microservice.domain.result.PageBean;

public interface ProductCategoryService {
	/**
	 * 增加产品类别
	 * @param productCategory
	 */
	public void insert(ProductCategory productCategory);
	
	/**
	 * 修改产品类别
	 * @param productCategory
	 */
	public void update(ProductCategory productCategory);
	
	/**
	 * 根据名称分页查询产品类别
	 * @param productCategory 包含名称的实体
	 * @param pageNumber 页号
	 * @param pageSize 每页记录数
	 * @return 分页对象
	 */
	public PageBean<ProductCategory> listByCriteria(ProductCategory productCategory, int pageNumber, int pageSize);
	
	/**
	 * 获取所有的产品类别
	 * @return 
	 */
	public List<ProductCategory> listAll();
	
	/**
	 * 根据主键获取产品类别
	 * @param categoryId
	 * @return
	 */
	public ProductCategory getById(String categoryId);
}
