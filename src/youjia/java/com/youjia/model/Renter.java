package com.youjia.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.DigestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.annotation.ModelMetaData;
import com.polarj.model.component.PersonalPhoto;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ModelMetaData(label = "租户管理")
@Entity
@Table(name = "renter")
public @ToString @EqualsAndHashCode(callSuper = false) class Renter extends GenericDbInfo {

	/**
	* 
	*/
	private static final long serialVersionUID = 4339686661945374242L;
	
	@FieldMetaData(position = 0, label = "身份证")
	@Column(name = "idCard", nullable = false)
	private @Setter @Getter String idCard;

	@FieldMetaData(position = 10, label = "手机号")
	@Column(name = "phone", nullable = false)
	private @Setter @Getter String phone;
	
	@FieldMetaData(position = 20, label = "姓名")
	@Column(name = "name", nullable = false)
	private @Setter @Getter String name;
	
    @JsonIgnore
    @Column(name = "password", length = 128, nullable = false)
    private @Getter String password;

    @FieldMetaData(position = 30, label = "身份证照片", embedded = true, dataType = FieldMetaDataSupportedDataType.FILE)
    @Embedded
    private @Getter @Setter PersonalPhoto photo;
    
    public boolean matchPassword(String password) {
		return this.password.equals(DigestUtils.md5DigestAsHex(password.getBytes()));
	}
	public void setPassword(String password) {
		this.password = DigestUtils.md5DigestAsHex(password.getBytes());
	}

}
