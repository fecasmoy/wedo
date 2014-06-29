package com.wedo.bserver.persistence.entities;

import java.io.Serializable;

/**
 * 域对象抽象类
 * 
 * @author isaac
 */
public abstract class AbstractEntity implements Serializable {

	// 进行序列化的时候用到的序列码
	private static final long serialVersionUID = -3443100689712081969L;

	/**
	 * getter id
	 * 
	 * @return return
	 */
	public abstract Long getId();

	/**
	 * equals method
	 * 
	 * @param object
	 *            object
	 * @return boolean {@link Boolean}
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AbstractEntity)) {
			return false;
		}
		AbstractEntity other = (AbstractEntity) object;
		return getId().equals(other.getId());
	}

	/**
	 * hash code method
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		if (null == getId()) {
			return super.hashCode();
		}
		return getId().hashCode();
	}
}
