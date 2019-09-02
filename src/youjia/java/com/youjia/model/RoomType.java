package com.youjia.model;

import com.polarj.model.enumeration.I18nResourceKey;

public enum RoomType implements I18nResourceKey {

	/**
	 * 单间
	 */
	单间,
	/**
	 * 一室一厅
	 */
	一室一厅,

	/**
	 * 二室一厅
	 */
	二室一厅,

	/**
	 * 三室一厅
	 */
	三室一厅,
	
	/**
	 * 三室二厅
	 */
	三室二厅;

	@Override
	public String getI18nResourceKey() {
		return name();
	}

}
