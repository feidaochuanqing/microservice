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

import com.founder.microservice.domain.ProductCategory;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.repository.ProductCategoryRepository;
import com.founder.microservice.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Override
	public void insert(ProductCategory order) {
		this.productCategoryRepository.save(order);
	}

	@Override
	@Transactional
	@Modifying
	public void update(ProductCategory product) {
		ProductCategory orderInDb = this.productCategoryRepository.findOne(product.getId());
		orderInDb.setCategoryCode(product.getCategoryCode());
		orderInDb.setCategoryName(product.getCategoryName());
		orderInDb.setLastModifyTime(new Date());
		orderInDb.setLastModifyUser(product.getLastModifyUser());
	}

	@Override
	@Transactional
	@Modifying
	public void deleteLogic(ProductCategory product) {
		ProductCategory orderInDb = this.productCategoryRepository.findOne(product.getId());
		orderInDb.setIsDeleted(true);
		orderInDb.setDeleteTime(new Date());
		orderInDb.setDeleteUser(product.getDeleteUser());
	}

	@Override
	@Transactional
	@Modifying
	public void delete(ProductCategory product) {
		ProductCategory orderInDb = this.productCategoryRepository.findOne(product.getId());
		this.productCategoryRepository.delete(orderInDb);
	}

	@Override
	public PageBean<ProductCategory> listByCriteria(final ProductCategory category, int pageNumber, int pageSize) {
		Page<ProductCategory> page = this.productCategoryRepository.findAll(new Specification<ProductCategory>() {
			@Override
			public Predicate toPredicate(Root<ProductCategory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> nameExp = root.get("categoryName");
				Predicate p1 = cb.like(nameExp, "%" + category.getCategoryName());
				Path<Boolean> isDeleteExp = root.get("isDeleted");
				Predicate p2 = cb.isFalse(isDeleteExp);
				return cb.and(p2, cb.and(p1));
			}

		}, new PageRequest(pageNumber - 1, pageSize, new Sort(Direction.DESC, new String[] { "id" })));

		return new PageBean<ProductCategory>(pageNumber, pageSize, (int) page.getTotalElements(), page.getContent());
	}

	@Override
	public ProductCategory getById(String categoryId) {
		return this.productCategoryRepository.findOne(categoryId);
	}

	@Override
	public List<ProductCategory> listAll() {
		return this.productCategoryRepository.findAll(new Specification<ProductCategory>() {
			@Override
			public Predicate toPredicate(Root<ProductCategory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<Boolean> isDeleteExp = root.get("isDeleted");
				Predicate p = cb.isFalse(isDeleteExp);
				return cb.and(p);
			}
		});
	}

}
