package com.youjia.model;

import com.polarj.model.enumeration.I18nResourceKey;

public enum PayStatus implements I18nResourceKey {

	/**
	 * 未交租
	 */
	UNPAYED,

	/**
	 * 已交租
	 */
	PAYED;
	
	@Override
	public String getI18nResourceKey() {
		return name();
	}

}
