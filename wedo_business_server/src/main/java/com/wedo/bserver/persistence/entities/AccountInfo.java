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
 * @description 详细的帐号信息
 * @tablename CREATE TABLE `wd_account`
 */
@Entity
@Table(name = "wd_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountInfo extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 833822940233157746L;

	/**
	 * `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	 */
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	private long id;

	/**
	 * `guid` VARCHAR(50) NOT NULL DEFAULT '0',
	 */
	@Column(name = "guid")
	private String guid;

	/**
	 * `real_name` VARCHAR(50) NOT NULL DEFAULT '0',
	 */
	@Column(name = "real_name")
	private String realName;

	/**
	 * `paper_type` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	 */
	@Column(name = "paper_type")
	private int paperType;

	/**
	 * `paper_id` VARCHAR(50) NOT NULL DEFAULT '0',
	 */
	@Column(name = "paper_id")
	private String paperId;

	/**
	 * `gender` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	 */
	@Column(name = "gender")
	private int gender;

	/**
	 * `birthday` INT UNSIGNED NOT NULL DEFAULT '0',
	 */
	@Column(name = "birthday")
	private int birthday;

	/**
	 * `contact_address` TEXT NOT NULL,
	 */
	@Column(name = "contact_address")
	private String contactAddress;

	/**
	 * `phone` VARCHAR(50) NOT NULL DEFAULT '0',
	 */
	@Column(name = "phone")
	private String phone;

	/**
	 * `cell_phone` VARCHAR(50) NOT NULL DEFAULT '0',
	 */
	@Column(name = "cell_phone")
	private String cellPhone;

	/**
	 * `email` VARCHAR(50) NOT NULL DEFAULT '0',
	 */
	@Column(name = "email")
	private String email;

	/**
	 * `account_sign` TEXT NOT NULL,
	 */
	@Column(name = "account_sign")
	private String accountSign;

	/**
	 * `remark` TEXT NOT NULL,
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * @return the id
	 */
	@Override
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
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the paperType
	 */
	public int getPaperType() {
		return paperType;
	}

	/**
	 * @param paperType
	 *            the paperType to set
	 */
	public void setPaperType(int paperType) {
		this.paperType = paperType;
	}

	/**
	 * @return the paperId
	 */
	public String getPaperId() {
		return paperId;
	}

	/**
	 * @param paperId
	 *            the paperId to set
	 */
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return the birthday
	 */
	public int getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(int birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the contactAddress
	 */
	public String getContactAddress() {
		return contactAddress;
	}

	/**
	 * @param contactAddress
	 *            the contactAddress to set
	 */
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return cellPhone;
	}

	/**
	 * @param cellPhone
	 *            the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	/**
	 * @return the accountSign
	 */
	public String getAccountSign() {
		return accountSign;
	}

	/**
	 * @param accountSign
	 *            the accountSign to set
	 */
	public void setAccountSign(String accountSign) {
		this.accountSign = accountSign;
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

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccountInfo [id=" + id + ", guid=" + guid + ", realName="
				+ realName + ", paperType=" + paperType + ", paperId="
				+ paperId + ", gender=" + gender + ", birthday=" + birthday
				+ ", contactAddress=" + contactAddress + ", phone=" + phone
				+ ", cellPhone=" + cellPhone + ", email=" + email
				+ ", accountSign=" + accountSign + ", remark=" + remark + "]";
	}

}
