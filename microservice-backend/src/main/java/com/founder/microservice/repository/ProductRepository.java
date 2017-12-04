package com.founder.microservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.founder.microservice.domain.Product;

public interface ProductRepository extends JpaSpecificationExecutor<Product>, JpaRepository<Product, String> {
	@Query(value="select * from masterdata_product where fk_product_category = ?1 and is_deleted = 0" ,nativeQuery=true)
	public List<Product> findByCategoryId(String categoryId);
}