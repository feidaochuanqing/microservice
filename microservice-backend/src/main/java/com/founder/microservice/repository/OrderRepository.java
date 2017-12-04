package com.founder.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.founder.microservice.domain.Order;

/**
 * 订单Repository
 * @author: lizhch
 * @date: 2017年12月4日 下午4:56:11
 * @version V1.0
 */
public interface OrderRepository extends JpaSpecificationExecutor<Order>, JpaRepository<Order, String> {
}