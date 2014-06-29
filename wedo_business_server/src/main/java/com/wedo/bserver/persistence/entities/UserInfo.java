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
 * @description 登录用的帐号信息
 * @tablename CREATE TABLE `wd_user`
 * 
 */
@Entity
@Table(name = "wd_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserInfo extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8364009086120082021L;

	/**
	 * `id` BIGINT(20) UNSIGNED NOT NULL auto_increment,
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
	 * `nick_name` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "nick_name")
	private String nickName;

	/**
	 * `user_name` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * `password` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "password")
	private String password;

	/**
	 * `user_type` TINYINT(3) UNSIGNED NOT NULL,
	 */
	@Column(name = "user_type")
	private int userType;

	/**
	 * `user_grade` INT(10) UNSIGNED NOT NULL,
	 */
	@Column(name = "user_grade")
	private int userGrade;

	/**
	 * `create_time` INT(10) UNSIGNED NOT NULL,
	 */
	@Column(name = "create_time")
	private int createTime;

	/**
	 * `last_login_time` INT(10) UNSIGNED NOT NULL,
	 */
	@Column(name = "last_login_time")
	private int lastLoginTime;

	/**
	 * `user_remark` TEXT,
	 */
	@Column(name = "user_remark")
	private String userRemark;

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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userType
	 */
	public int getUserType() {
		return userType;
	}

	/**
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

	/**
	 * @return the userGrade
	 */
	public int getUserGrade() {
		return userGrade;
	}

	/**
	 * @param userGrade
	 *            the userGrade to set
	 */
	public void setUserGrade(int userGrade) {
		this.userGrade = userGrade;
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
	 * @return the lastLoginTime
	 */
	public int getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime
	 *            the lastLoginTime to set
	 */
	public void setLastLoginTime(int lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the userRemark
	 */
	public String getUserRemark() {
		return userRemark;
	}

	/**
	 * @param userRemark
	 *            the userRemark to set
	 */
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", guid=" + guid + ", nickName="
				+ nickName + ", userName=" + userName + ", password="
				+ password + ", userType=" + userType + ", userGrade="
				+ userGrade + ", createTime=" + createTime + ", lastLoginTime="
				+ lastLoginTime + ", userRemark=" + userRemark + "]";
	}

}
