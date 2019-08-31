package com.youjia.model;

import com.polarj.model.enumeration.I18nResourceKey;

public enum RoomType implements I18nResourceKey {

	/**
	 * 单间
	 */
	ONE_ROOM,
	/**
	 * 一室一厅
	 */
	ONE_ROOM_ONE_HALL,

	/**
	 * 二室一厅
	 */
	TWO_ROOM_ONE_HALL,

	/**
	 * 三室一厅
	 */
	THREE_ROOM_ONE_HALL,
	
	/**
	 * 三室二厅
	 */
	THREE_ROOM_TWO_HALL;

	@Override
	public String getI18nResourceKey() {
		return name();
	}

}
