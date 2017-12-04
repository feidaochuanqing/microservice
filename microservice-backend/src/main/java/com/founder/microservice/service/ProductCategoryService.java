package com.founder.microservice.service;

import java.util.List;

import com.founder.microservice.domain.ProductCategory;
import com.founder.microservice.domain.result.PageBean;

public interface ProductCategoryService {
	public void insert(ProductCategory productCategory);
	public void update(ProductCategory productCategory);
	public void delete(ProductCategory productCategory);
	public void deleteLogic(ProductCategory productCategory);
	public PageBean<ProductCategory> listByCriteria(ProductCategory productCategory, int pageNumber, int pageSize);
	public List<ProductCategory> listAll();
	public ProductCategory getById(String categoryId);
}
