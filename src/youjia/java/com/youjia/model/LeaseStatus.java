package com.youjia.model;

import com.polarj.model.enumeration.I18nResourceKey;

public enum LeaseStatus implements I18nResourceKey {

	/**
	 * 生效
	 */
	生效,

	/**
	 * 申请退租
	 */
	申请退租,

	/**
	 * 已退租
	 */
	已退租,

	/**
	 * 到期
	 */
	到期;

	@Override
	public String getI18nResourceKey() {
		return name();
	}

}
