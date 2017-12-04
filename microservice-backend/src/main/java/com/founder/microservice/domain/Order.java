package com.founder.microservice.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity  
@GenericGenerator(name="uuid_s",strategy="uuid") 
@Table(name="trade_order")  
public class Order implements Serializable {
	private static final long serialVersionUID = 4759972575472717055L;
	
	@Id  
	private String id;
	
	@ManyToOne
	@JoinColumn(name="fk_product")
	private Product product;
	
	@Column(name="count")  
	private Integer count;
	
	@Column(name="address")  
	private String address;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_time")  
	private Date createTime;
	
	@Column(name="create_user")  
	private String createUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="last_modify_time")  
	private Date lastModifyTime;
	
	@Column(name="last_modify_user")  
	private String lastModifyUser;
	
	@Column(name="is_deleted")  
	private Boolean isDeleted;
	
	@Temporal(TemporalType.DATE)
	@Column(name="delete_time")  
	private Date deleteTime;
	
	@Column(name="delete_user")  
	private String deleteUser;
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
}
