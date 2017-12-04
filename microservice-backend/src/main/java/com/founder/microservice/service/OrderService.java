package com.founder.microservice.service;

import com.founder.microservice.domain.Order;
import com.founder.microservice.domain.result.PageBean;

public interface OrderService {
	public void insert(Order order);
	public void update(Order order);
	public void delete(Order order);
	public void deleteLogic(Order order);
	public PageBean<Order> listByProductName(String productName, int pageNumber, int pageSize);
	public Order getById(String orderId);
}
