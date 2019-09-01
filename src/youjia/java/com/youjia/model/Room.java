package com.youjia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "房间管理")
@Entity
@Table(name = "room")
public @ToString @EqualsAndHashCode(callSuper = false) class Room extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;

	@FieldMetaData(position = 0, label = "房屋", dataType = FieldMetaDataSupportedDataType.OBJECT, managementSeparately = true, labelField = "label", required = true, enumClass = Building.class)
	@ManyToOne
	@JoinColumn(name = "buildingId", foreignKey = @ForeignKey(name = "fk_room_building_buildingId"), referencedColumnName = "id")
	private @Setter @Getter Building building;

	@FieldMetaData(position = 10, label = "房间编号")
	@Column(name = "roomNo", nullable = false)
	private @Setter @Getter String roomNo;

	@FieldMetaData(position = 20, label = "房间类型", required = true, enumClass = RoomType.class)
	@Column(name = "roomType")
	private @Setter @Getter String roomType;

	@FieldMetaData(position = 30, label = "房间状态", required = true, enumClass = RoomStatus.class)
	@Column(name = "roomStatus")
	private @Setter @Getter String roomStatus;

	@FieldMetaData(position = 40, label = "是否有厨房", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
	@Column(name = "hasKitchen")
	private @Setter @Getter boolean hasKitchen;

	@FieldMetaData(position = 50, label = "是否有洗手间", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
	@Column(name = "hasWashroom")
	private @Setter @Getter boolean hasWashroom;

	@FieldMetaData(position = 55, label = "家具")
	@Column(name = "furniture")
	private @Setter @Getter String furniture;

	@FieldMetaData(position = 60, label = "房间描述")
	@Column(name = "description")
	private @Setter @Getter String description;

	@FieldMetaData(position = 70, label = "默认房租", dataType = FieldMetaDataSupportedDataType.NUMBER)
	@Column(name = "defaultRent")
	private @Setter @Getter Integer defaultRent;

	private @Setter @Getter String label;

}
