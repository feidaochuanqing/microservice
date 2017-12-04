package com.founder.microservice.service;

import java.util.List;

import com.founder.microservice.domain.Product;
import com.founder.microservice.domain.result.PageBean;

public interface ProductService {
	void insert(Product product);
	void update(Product product);
	void deleteLogic(Product product);
	void delete(Product product);
	PageBean<Product> listByCriteria(Product product, int pageNumber, int pageSize);
	Product getById(String productId);
	List<Product> listAll();
	List<Product> listByCategoryId(String categoryId);
}
