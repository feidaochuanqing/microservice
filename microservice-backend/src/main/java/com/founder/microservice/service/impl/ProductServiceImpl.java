package com.founder.microservice.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.founder.microservice.domain.Product;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.repository.ProductRepository;
import com.founder.microservice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	@Transactional
	public void insert(Product order) {
		this.productRepository.save(order);
	}

	@Override
	@Transactional
	@Modifying
	public void update(Product product) {
		Product orderInDb = this.productRepository.findOne(product.getId());
		orderInDb.setProductCode(orderInDb.getProductCode());
		orderInDb.setProductName(product.getProductName());
		orderInDb.setProductCategory(product.getProductCategory());
		orderInDb.setLastModifyTime(new Date());
		orderInDb.setLastModifyUser(product.getLastModifyUser());
	}

	@Override
	@Transactional
	@Modifying
	public void deleteLogic(Product product) {
		Product orderInDb = this.productRepository.findOne(product.getId());
		orderInDb.setIsDeleted(true);
		orderInDb.setDeleteTime(new Date());
		orderInDb.setDeleteUser(product.getDeleteUser());
	}

	@Override
	@Transactional
	@Modifying
	public void delete(Product product) {
		Product orderInDb = this.productRepository.findOne(product.getId());
		this.productRepository.delete(orderInDb);
	}

	@Override
	public PageBean<Product> listByCriteria(final Product product, int pageNumber, int pageSize) {
		Page<Product> page = this.productRepository.findAll(new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> nameExp = root.get("productName");
				Predicate p1= cb.like(nameExp, "%" + product.getProductName() + "%");
				
				Path<Boolean> isDeleteExp = root.get("isDeleted");
				Predicate p2= cb.isFalse(isDeleteExp);
				return cb.and(p2,cb.and(p1));
			}

		}, new PageRequest(pageNumber - 1, pageSize, new Sort(Direction.DESC, new String[] { "id" })));
		return new PageBean<Product>(pageNumber, pageSize, (int)page.getTotalElements(), page.getContent());
	}

	@Override
	public Product getById(String productId) {
		return this.productRepository.findOne(productId);
	}
	@Override
	public List<Product> listAll() {
		return this.productRepository.findAll();
	}

	@Override
	public List<Product> listByCategoryId(String categoryId) {
		return this.productRepository.findByCategoryId(categoryId);
	}
}
