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
 * @description 商品详细信息
 * @tablename CREATE TABLE `wd_goods_detail`
 */
@Entity
@Table(name = "wd_goods_detail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GoodsDetail extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6624788502847581566L;

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
	 * `name` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "name")
	private String name;

	/**
	 * `class_guid` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "class_guid")
	private String classGuid;

	/**
	 * `status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
	 */
	@Column(name = "status")
	private int status;

	/**
	 * `upcart_time` INT(10) UNSIGNED NOT NULL DEFAULT '0',
	 */
	@Column(name = "upcart_time")
	private int upcartTime;

	/**
	 * `downcart_time` INT(10) UNSIGNED NOT NULL DEFAULT '0',
	 */
	@Column(name = "downcart_time")
	private int downcartTime;

	/**
	 * `goods_pic` TEXT NOT NULL,
	 */
	@Column(name = "goods_pic")
	private String goodsPic;

	/**
	 * `goods_info_url` TEXT NOT NULL,
	 */
	@Column(name = "goods_info_url")
	private String goodsInfoUrl;

	/**
	 * `goods_description` TEXT NOT NULL,
	 */
	@Column(name = "goods_description")
	private String goodsDescription;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the classGuid
	 */
	public String getClassGuid() {
		return classGuid;
	}

	/**
	 * @param classGuid
	 *            the classGuid to set
	 */
	public void setClassGuid(String classGuid) {
		this.classGuid = classGuid;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the upcartTime
	 */
	public int getUpcartTime() {
		return upcartTime;
	}

	/**
	 * @param upcartTime
	 *            the upcartTime to set
	 */
	public void setUpcartTime(int upcartTime) {
		this.upcartTime = upcartTime;
	}

	/**
	 * @return the downcartTime
	 */
	public int getDowncartTime() {
		return downcartTime;
	}

	/**
	 * @param downcartTime
	 *            the downcartTime to set
	 */
	public void setDowncartTime(int downcartTime) {
		this.downcartTime = downcartTime;
	}

	/**
	 * @return the goodsPic
	 */
	public String getGoodsPic() {
		return goodsPic;
	}

	/**
	 * @param goodsPic
	 *            the goodsPic to set
	 */
	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	/**
	 * @return the goodsInfoUrl
	 */
	public String getGoodsInfoUrl() {
		return goodsInfoUrl;
	}

	/**
	 * @param goodsInfoUrl
	 *            the goodsInfoUrl to set
	 */
	public void setGoodsInfoUrl(String goodsInfoUrl) {
		this.goodsInfoUrl = goodsInfoUrl;
	}

	/**
	 * @return the goodsDescription
	 */
	public String getGoodsDescription() {
		return goodsDescription;
	}

	/**
	 * @param goodsDescription
	 *            the goodsDescription to set
	 */
	public void setGoodsDescription(String goodsDescription) {
		this.goodsDescription = goodsDescription;
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
		return "GoodsDetail [id=" + id + ", guid=" + guid + ", name=" + name
				+ ", classGuid=" + classGuid + ", status=" + status
				+ ", upcartTime=" + upcartTime + ", downcartTime="
				+ downcartTime + ", goodsPic=" + goodsPic + ", goodsInfoUrl="
				+ goodsInfoUrl + ", goodsDescription=" + goodsDescription
				+ ", remark=" + remark + "]";
	}

}
