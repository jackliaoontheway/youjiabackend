package com.youjia.model;

import com.polarj.model.enumeration.I18nResourceKey;

public enum RoomStatus implements I18nResourceKey {

	/**
	 * 未租
	 */
	未租,

	/**
	 * 预定
	 */
	预定,

	/**
	 * 已租
	 */
	已租,

	/**
	 * 正在退租
	 */
	正在退租;

	@Override
	public String getI18nResourceKey() {
		return name();
	}

}
