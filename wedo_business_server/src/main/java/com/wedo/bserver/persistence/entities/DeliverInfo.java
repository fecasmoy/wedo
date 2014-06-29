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
 * @description 发货地址信息
 * @tablename CREATE TABLE `wd_deliver_info`
 */
@Entity
@Table(name = "wd_deliver_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DeliverInfo extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1617854892022393785L;

	/**
	 * `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
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
	 * `alias` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "alias")
	private String alias;

	/**
	 * `contact_name` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "contact_name")
	private String contactName;

	/**
	 * `contact_phone` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "contact_phone")
	private String contactPhone;

	/**
	 * `address` TEXT NOT NULL,
	 */
	@Column(name = "address")
	private String address;

	/**
	 * `remark` TEXT NOT NULL,
	 */
	@Column(name = "name")
	private String name;

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
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName
	 *            the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the contactPhone
	 */
	public String getContactPhone() {
		return contactPhone;
	}

	/**
	 * @param contactPhone
	 *            the contactPhone to set
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeliverInfo [id=" + id + ", guid=" + guid + ", alias=" + alias
				+ ", contactName=" + contactName + ", contactPhone="
				+ contactPhone + ", address=" + address + ", name=" + name
				+ "]";
	}

}
