package com.founder.microservice.service;

import java.util.List;

import com.founder.microservice.domain.Product;
import com.founder.microservice.domain.result.PageBean;

public interface ProductService {
	/**
	 * 增加产品
	 * @param product
	 */
	void insert(Product product);
	
	/**
	 * 修改产品
	 * @param product
	 */
	void update(Product product);
	
	/**
	 * 根据名称分页查询
	 * @param product 查询实体
	 * @param pageNumber 页号
	 * @param pageSize 每页大小
	 * @return
	 */
	PageBean<Product> listByCriteria(Product product, int pageNumber, int pageSize);
	
	/**
	 * 根据主键获取产品
	 * @param productId 主键
	 * @return
	 */
	Product getById(String productId);
	
	/**
	 * 获取所有的产品
	 * @return
	 */
	List<Product> listAll();
	
	/**
	 * 根据产品类别主键获取产品
	 * @param categoryId
	 * @return
	 */
	List<Product> listByCategoryId(String categoryId);
}
