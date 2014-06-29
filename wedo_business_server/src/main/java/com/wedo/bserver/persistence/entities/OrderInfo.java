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
 * @description 订单信息
 * @tablename CREATE TABLE `wd_order`
 */
@Entity
@Table(name = "wd_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderInfo extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5008662887868384407L;

	/**
	 * `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	 */
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	private long id;

	/**
	 * `guid` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "guid")
	private String guid;

	/**
	 * `user_guid` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "user_guid")
	private String userGuid;

	/**
	 * `order_type` TINYINT(3) UNSIGNED NOT NULL,
	 */
	@Column(name = "order_type")
	private int orderType;

	/**
	 * `order_status` TINYINT(3) UNSIGNED NOT NULL,
	 */
	@Column(name = "order_status")
	private int order_status;

	/**
	 * `goods_guid` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "goods_guid")
	private String goodsGuid;

	/**
	 * `create_time` INT(10) UNSIGNED NOT NULL,
	 */
	@Column(name = "create_time")
	private int createTime;

	/**
	 * `finish_time` INT(10) UNSIGNED NOT NULL,
	 */
	@Column(name = "finish_time")
	private int finishTime;

	/**
	 * `remark` TEXT NOT NULL,
	 */
	@Column(name = "remark")
	private String remark;

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
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid
	 *            the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the userGuid
	 */
	public String getUserGuid() {
		return userGuid;
	}

	/**
	 * @param userGuid
	 *            the userGuid to set
	 */
	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	/**
	 * @return the orderType
	 */
	public int getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType
	 *            the orderType to set
	 */
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the order_status
	 */
	public int getOrder_status() {
		return order_status;
	}

	/**
	 * @param order_status
	 *            the order_status to set
	 */
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}

	/**
	 * @return the goodsGuid
	 */
	public String getGoodsGuid() {
		return goodsGuid;
	}

	/**
	 * @param goodsGuid
	 *            the goodsGuid to set
	 */
	public void setGoodsGuid(String goodsGuid) {
		this.goodsGuid = goodsGuid;
	}

	/**
	 * @return the createTime
	 */
	public int getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the finishTime
	 */
	public int getFinishTime() {
		return finishTime;
	}

	/**
	 * @param finishTime
	 *            the finishTime to set
	 */
	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderInfo [id=" + id + ", guid=" + guid + ", userGuid="
				+ userGuid + ", orderType=" + orderType + ", order_status="
				+ order_status + ", goodsGuid=" + goodsGuid + ", createTime="
				+ createTime + ", finishTime=" + finishTime + ", remark="
				+ remark + "]";
	}

}
