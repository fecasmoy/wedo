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
 * @description 商品评价信息
 * @tablename CREATE TABLE `wd_goods_evaluation`
 */
@Entity
@Table(name = "wd_goods_evaluation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GoodsEvaluation extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6401315777459060244L;

	/**
	 * `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	 */
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	private long id;

	/**
	 * `user_guid` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "user_guid")
	private String userGuid;

	/**
	 * `goods_guid` VARCHAR(50) NOT NULL,
	 */
	@Column(name = "goods_guid")
	private String goodsGuid;

	/**
	 * `evalue_time` INT(10) UNSIGNED NOT NULL DEFAULT '0',
	 */
	@Column(name = "evalue_time")
	private int evalueTime;

	/**
	 * `evaluation` TEXT NOT NULL,
	 */
	@Column(name = "evaluation")
	private String evaluation;

	/**
	 * `evaluation_pic_urls` TEXT NOT NULL,
	 */
	@Column(name = "evaluation_pic_urls")
	private String evaluationPicUrls;

	/**
	 * `score` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
	 */
	@Column(name = "score")
	private int score;

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
	 * @return the evalueTime
	 */
	public int getEvalueTime() {
		return evalueTime;
	}

	/**
	 * @param evalueTime
	 *            the evalueTime to set
	 */
	public void setEvalueTime(int evalueTime) {
		this.evalueTime = evalueTime;
	}

	/**
	 * @return the evaluation
	 */
	public String getEvaluation() {
		return evaluation;
	}

	/**
	 * @param evaluation
	 *            the evaluation to set
	 */
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	/**
	 * @return the evaluationPicUrls
	 */
	public String getEvaluationPicUrls() {
		return evaluationPicUrls;
	}

	/**
	 * @param evaluationPicUrls
	 *            the evaluationPicUrls to set
	 */
	public void setEvaluationPicUrls(String evaluationPicUrls) {
		this.evaluationPicUrls = evaluationPicUrls;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(int score) {
		this.score = score;
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
		return "GoodsEvaluation [id=" + id + ", userGuid=" + userGuid
				+ ", goodsGuid=" + goodsGuid + ", evalueTime=" + evalueTime
				+ ", evaluation=" + evaluation + ", evaluationPicUrls="
				+ evaluationPicUrls + ", score=" + score + ", remark=" + remark
				+ "]";
	}

}
