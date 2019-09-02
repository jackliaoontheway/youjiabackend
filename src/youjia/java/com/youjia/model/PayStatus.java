package com.youjia.model;

import com.polarj.model.enumeration.I18nResourceKey;

public enum PayStatus implements I18nResourceKey {

	/**
	 * 未交租
	 */
	未交租,

	/**
	 * 已交租
	 */
	已交租;
	
	@Override
	public String getI18nResourceKey() {
		return name();
	}

}
