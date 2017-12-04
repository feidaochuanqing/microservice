package com.founder.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.founder.microservice.domain.ProductCategory;

public interface ProductCategoryRepository extends  JpaSpecificationExecutor<ProductCategory>,JpaRepository<ProductCategory, String> {
}