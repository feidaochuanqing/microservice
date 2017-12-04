package com.founder.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.founder.microservice.domain.ProductCategory;
/**
 * 产品类别Repository
 * @author: lizhch
 * @date: 2017年12月4日 下午4:56:57  
 * @version V1.0
 */
public interface ProductCategoryRepository extends  JpaSpecificationExecutor<ProductCategory>,JpaRepository<ProductCategory, String> {
}