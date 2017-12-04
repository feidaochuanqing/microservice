package com.founder.microservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.founder.microservice.domain.Product;

/**
 * 产品Repository
 * @author: lizhch
 * @date: 2017年12月4日 下午4:57:13  
 * @version V1.0
 */
public interface ProductRepository extends JpaSpecificationExecutor<Product>, JpaRepository<Product, String> {
	/**
	 * 获取某一个产品类别下的所有的产品
	 * @param categoryId
	 * @return
	 */
	@Query(value="select * from masterdata_product where fk_product_category = ?1 and is_deleted = 0" ,nativeQuery=true)
	public List<Product> findByCategoryId(String categoryId);
}