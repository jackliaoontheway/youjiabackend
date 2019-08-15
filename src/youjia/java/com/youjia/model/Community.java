package com.youjia.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "社区管理")
@Entity
@Table(name = "community")
public @ToString @EqualsAndHashCode(callSuper = false) class Community extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;

	@FieldMetaData(position = 0, label = "社区名称")
	@Column(name = "name", nullable = false)
	private @Setter @Getter String name;

	@FieldMetaData(position = 10, label = "社区地址")
	@Column(name = "address")
	private @Setter @Getter String address;

	@FieldMetaData(position = 20, label = "社区描述")
	@Column(name = "description")
	private @Setter @Getter String description;

	@OneToMany(mappedBy = "community")
	private List<Building> buildingList;

}
