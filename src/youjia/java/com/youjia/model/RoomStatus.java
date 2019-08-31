package com.youjia.model;

import com.polarj.model.enumeration.I18nResourceKey;

public enum RoomStatus implements I18nResourceKey {

	/**
	 * 未租
	 */
	AVAILABEL,

	/**
	 * 预定
	 */
	RESERVED,

	/**
	 * 已租
	 */
	RENTED,

	/**
	 * 正在退租
	 */
	WITHDRAW;

	@Override
	public String getI18nResourceKey() {
		return name();
	}

}
