package com.youjia.model;

import com.polarj.model.enumeration.I18nResourceKey;

public enum LeaseStatus implements I18nResourceKey {

	/**
	 * 生效
	 */
	AVAILABEL,

	/**
	 * 申请退租
	 */
	WITHDRAWREQUEST,

	/**
	 * 已退租
	 */
	WITHDRAWED,

	/**
	 * 到期
	 */
	EXPIRED;

	@Override
	public String getI18nResourceKey() {
		return name();
	}

}
