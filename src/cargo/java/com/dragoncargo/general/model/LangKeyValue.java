package com.dragoncargo.general.model;

import lombok.Getter;
import lombok.Setter;

public class LangKeyValue {
	// {code,lkey}->lvalue;
	// {JC,cn}->机场
	// {JC,en} -> airport
	// 唯一识别代码
	private @Setter @Getter String code;

	// 语言类别 cn,en，唯一识别
	private @Setter @Getter String lkey;

	// 语言具体值
	private @Setter @Getter String lvalue;
}
