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
 * @description 商品类目信息
 * @tablename CREATE TABLE `wd_goods_classification`
 */
@Entity
@Table(name = "wd_goods_classification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GoodsClassification extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8347446038365509293L;

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
	 * `class_name` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "class_name")
	private String className;

	/**
	 * `class_parent` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "class_parent")
	private String classParent;

	/**
	 * `class_type` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "class_type")
	private int classType;

	/**
	 * `description` TEXT NOT NULL,
	 */
	@Column(name = "description")
	private String description;

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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the classParent
	 */
	public String getClassParent() {
		return classParent;
	}

	/**
	 * @param classParent
	 *            the classParent to set
	 */
	public void setClassParent(String classParent) {
		this.classParent = classParent;
	}

	/**
	 * @return the classType
	 */
	public int getClassType() {
		return classType;
	}

	/**
	 * @param classType
	 *            the classType to set
	 */
	public void setClassType(int classType) {
		this.classType = classType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
		return "GoodsClassification [id=" + id + ", guid=" + guid
				+ ", className=" + className + ", classParent=" + classParent
				+ ", classType=" + classType + ", description=" + description
				+ ", remark=" + remark + "]";
	}

}
