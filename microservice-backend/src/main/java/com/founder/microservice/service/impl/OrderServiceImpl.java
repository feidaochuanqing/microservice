package com.founder.microservice.service.impl;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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

import com.founder.microservice.domain.Order;
import com.founder.microservice.domain.Product;
import com.founder.microservice.domain.result.PageBean;
import com.founder.microservice.repository.OrderRepository;
import com.founder.microservice.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public void insert(Order order) {
		this.orderRepository.save(order);
	}

	@Override
	@Transactional
	@Modifying
	public void update(Order order) {
		Order orderInDb = this.orderRepository.findOne(order.getId());
		orderInDb.setAddress(order.getAddress());
		orderInDb.setCount(order.getCount());
		orderInDb.setProduct(order.getProduct());
		orderInDb.setLastModifyTime(new Date());
		orderInDb.setLastModifyUser(order.getLastModifyUser());
	}

	@Override
	public PageBean<Order> listByProductName(final String productName, int pageNumber, int pageSize) {
		Page<Order> page = this.orderRepository.findAll(new Specification<Order>() {
			@Override
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Order,Product> join = root.join("product", JoinType.INNER);
	            Path<String> exp3 = join.get("productName"); 
	            Predicate p1 = cb.like(exp3, "%"+productName+"%");
				Path<Boolean> isDeleteExp = root.get("isDeleted");
				Predicate p2= cb.isFalse(isDeleteExp);
				return cb.and(p2,cb.and(p1));
			}

		}, new PageRequest(pageNumber - 1 , pageSize, new Sort(Direction.DESC, new String[] { "lastModifyTime" })));
		return new PageBean<Order>(pageNumber, pageSize, (int)page.getTotalElements(), page.getContent());
	}

	@Override
	public Order getById(String orderId) {
		return this.orderRepository.getOne(orderId);
	}
}
