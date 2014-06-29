/**
 * 
 */
package com.wedo.bserver.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author isaac
 * @description 订单跟踪信息
 * @tablename CREATE TABLE `wd_order_trace`
 */
@Entity
@Table(name = "wd_order_trace")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderTrace extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5412879181810314474L;

	/**
	 * `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	 */
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	private long id;

	/**
	 * `order_guid` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "order_guid")
	private String orderGuid;

	/**
	 * `operate_time` INT(10) UNSIGNED NOT NULL,
	 */
	@Column(name = "operate_time")
	private int operateTime;

	/**
	 * `order_status` TINYINT(3) UNSIGNED NOT NULL,
	 */
	@Column(name = "order_status")
	private String orderStatus;

	/**
	 * `location` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "location")
	private String location;

	/**
	 * `operator` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "operator")
	private String operator;

	/**
	 * `operate_description` TEXT,
	 */
	@Column(name = "operate_description")
	private String operateDescription;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the orderGuid
	 */
	public String getOrderGuid() {
		return orderGuid;
	}

	/**
	 * @param orderGuid
	 *            the orderGuid to set
	 */
	public void setOrderGuid(String orderGuid) {
		this.orderGuid = orderGuid;
	}

	/**
	 * @return the operateTime
	 */
	public int getOperateTime() {
		return operateTime;
	}

	/**
	 * @param operateTime
	 *            the operateTime to set
	 */
	public void setOperateTime(int operateTime) {
		this.operateTime = operateTime;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus
	 *            the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the operateDescription
	 */
	public String getOperateDescription() {
		return operateDescription;
	}

	/**
	 * @param operateDescription
	 *            the operateDescription to set
	 */
	public void setOperateDescription(String operateDescription) {
		this.operateDescription = operateDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderTrace [id=" + id + ", orderGuid=" + orderGuid
				+ ", operateTime=" + operateTime + ", orderStatus="
				+ orderStatus + ", location=" + location + ", operator="
				+ operator + ", operateDescription=" + operateDescription + "]";
	}

}
