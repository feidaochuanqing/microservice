package com.founder.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.founder.microservice.domain.Order;

public interface OrderRepository extends JpaSpecificationExecutor<Order>, JpaRepository<Order, String> {
}