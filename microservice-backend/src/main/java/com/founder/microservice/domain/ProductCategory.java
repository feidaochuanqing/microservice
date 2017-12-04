package com.founder.microservice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 产品类别实体
 * @author: lizhch
 * @date: 2017年12月4日 下午4:53:32  
 * @version V1.0
 */
@Entity
@Table(name = "masterdata_product_category")
public class ProductCategory implements Serializable {

	private static final long serialVersionUID = -1877917314923361947L;

	@Id  
	private String id;

	@Column(name = "category_code")
	private String categoryCode;

	@Column(name = "category_name")
	private String categoryName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "create_user")
	private String createUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "last_modify_time")
	private Date lastModifyTime;
	
	@Column(name = "last_modify_user")
	private String lastModifyUser;
	
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	
	@Column(name = "delete_time")
	@Temporal(TemporalType.DATE)
	private Date deleteTime;
	
	@Column(name = "delete_user")
	private String deleteUser;
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name = "fk_product_category")
	private List<Product> products;
	
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
