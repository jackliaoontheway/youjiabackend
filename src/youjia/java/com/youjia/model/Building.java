package com.youjia.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "房屋管理")
@Entity
@Table(name = "building")
public @ToString @EqualsAndHashCode(callSuper = false) class Building extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;

	@FieldMetaData(position = 0, label = "社区", dataType = FieldMetaDataSupportedDataType.OBJECT, required = true, labelField = "name", enumClass = Community.class)
	@ManyToOne
	@JoinColumn(name = "communityId", foreignKey = @ForeignKey(name = "fk_building_community_communityId"), referencedColumnName = "id")
	private @Getter @Setter Community community;

	@FieldMetaData(position = 10, label = "房屋编号(栋)")
	@Column(name = "name", nullable = false)
	private @Setter @Getter String buildingNo;

	@FieldMetaData(position = 20, label = "描述")
	@Column(name = "description")
	private @Setter @Getter String description;

	@OneToMany(mappedBy = "building")
	private List<Room> roomList;

}
