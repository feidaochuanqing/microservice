package com.founder.microservice.service;

import com.founder.microservice.domain.Order;
import com.founder.microservice.domain.result.PageBean;

/**
 * 订单服务类
 * @author: lizhch
 * @date: 2017年12月4日 下午4:58:10  
 * @version V1.0
 */
public interface OrderService {
	/**
	 * 增加
	 * @param order
	 */
	public void insert(Order order);
	
	/**
	 * 修改
	 * @param order
	 */
	public void update(Order order);
	
	/**
	 * 根据产品名称分页查询订单
	 * @param productName 产品名称
	 * @param pageNumber 页号
	 * @param pageSize 每页大小 
	 * @return
	 */
	public PageBean<Order> listByProductName(String productName, int pageNumber, int pageSize);
	
	/**
	 * 根据主键获取订单
	 * @param orderId 订单主键
	 * @return
	 */
	public Order getById(String orderId);
}
