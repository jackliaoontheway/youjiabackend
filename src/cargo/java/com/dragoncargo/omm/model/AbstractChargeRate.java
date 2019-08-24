package com.dragoncargo.omm.model;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.dragoncargo.general.model.AirPortInfo;
import com.dragoncargo.general.model.ChargeUnit;
import com.dragoncargo.general.model.FeeInfo;
import com.dragoncargo.general.model.WeightType;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.HasOwner;
import com.polarj.model.UserAccount;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.enumeration.FieldMetaDataSupportedDataType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 报价
 */
@Entity
@Table(name = "chargerate")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "classification")
public @ToString @EqualsAndHashCode(callSuper = false) abstract class AbstractChargeRate extends GenericDbInfo
        implements HasOwner
{
    /**
     * 
     */
    private static final long serialVersionUID = -2797706849498084721L;

    /**
     * 深圳机场/广州机场
     */
    @FieldMetaData(position = 0, label = "AirPort", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = AirPortInfo.class, labelField = "code")
    @ManyToOne
    @JoinColumn(name = "airPortInfoId", foreignKey = @ForeignKey(name = "fk_chargerate_airportinfo_airportinfoid"),
            referencedColumnName = "id")
    private @Setter @Getter AirPortInfo airPortInfo;

    /**
     * ChargeRateType 公开报价,最低报价,销售底价,公司成本
     */
    @FieldMetaData(position = 5, label = "Charge Rate Type", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = ChargeRateType.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "chargeRateTypeId",
            foreignKey = @ForeignKey(name = "fk_chargerate_chargeratetype_chargeratetypeid"),
            referencedColumnName = "id")
    private @Setter @Getter ChargeRateType chargeRateType;

    /**
     * 报价项目
     */
    @FieldMetaData(position = 10, label = "Charge Rate Item", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = ChargeRateItem.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "chargeRateItemId",
            foreignKey = @ForeignKey(name = "fk_chargerate_chargerateitem_chargerateitemid"),
            referencedColumnName = "id")
    private @Setter @Getter ChargeRateItem chargeRateItem;

    /**
     * 计费单位, KG/LBS 票 操作次数 个 页 天
     */
    @FieldMetaData(position = 20, label = "Charge Unit", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = ChargeUnit.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "chargeUnitId", foreignKey = @ForeignKey(name = "fk_chargerate_chargeUnit_chargeUnitid"),
            referencedColumnName = "id")
    private @Setter @Getter ChargeUnit chargeUnit;
    
    /**
     * 当选择 计费单位为 重量时 需要填 重量类型 毛重 计费重
     */
    @FieldMetaData(position = 25, label = "Weight Type", dataType = FieldMetaDataSupportedDataType.OBJECT,
            enumClass = WeightType.class, labelField = "name")
    @ManyToOne
    @JoinColumn(name = "weightTypeId", foreignKey = @ForeignKey(name = "fk_chargerate_weighttype_weighttypeid"),
            referencedColumnName = "id")
    private @Setter @Getter WeightType weightType;

    /**
     * 计费单价
     */
    @FieldMetaData(position = 30, label = "Unit Price", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${currency}${amount}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currency", column = @Column(name = "unitPriceCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "unitPriceValue")) })
    private @Setter @Getter FeeInfo unitPrice;

    /**
     * 最低收费
     */
    @FieldMetaData(position = 40, label = "Minimum Charge", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${currency}${amount}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currency", column = @Column(name = "minimumChargeCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "minimumChargeValue")) })
    private @Setter @Getter FeeInfo minimumCharge;

    /**
     * 最高收费
     */
    @FieldMetaData(position = 50, label = "Maximum Charge", dataType = FieldMetaDataSupportedDataType.OBJECT,
            embedded = true, formatter = "${currency}${amount}")
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "currency", column = @Column(name = "maximumChargeCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "maximumChargeValue")) })
    private @Setter @Getter FeeInfo maximumCharge;

    /**
     * 报价适用范围
     */
    @FieldMetaData(position = 60, label = "Charge rate limitations", labelField = "description",
            dataType = FieldMetaDataSupportedDataType.ARRAY, enumClass = ChargeRateLimitation.class, multiChoice = true,
            managementSeparately = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "chargerateId", foreignKey = @ForeignKey(name = "fk_chargerate_chargelimitation_id"))
    private @Setter @Getter List<ChargeRateLimitation> limitationList;

    /**
     * 是否一定产生
     */
    @FieldMetaData(position = 65, label = "Estimate Available", dataType = FieldMetaDataSupportedDataType.BOOLEAN)
    @Column
    private @Setter @Getter boolean estimateAvailable;

    /**
     * 生效日期
     */
    @FieldMetaData(position = 70, label = "Effective Date", dataType = FieldMetaDataSupportedDataType.DATE,
            formatter = FieldMetaDataSupportedDataType.DATEFORMAT)
    @Column(length = 32)
    private @Setter @Getter Date effetiveDate;

    /**
     * 失效日期
     */
    @FieldMetaData(position = 80, label = "Expired Date", dataType = FieldMetaDataSupportedDataType.DATE,
            formatter = FieldMetaDataSupportedDataType.DATEFORMAT)
    @Column(length = 32)
    private @Setter @Getter Date expiredDate;

    /**
     * 报价项状态 Enable Disable
     */
    @FieldMetaData(position = 90, label = "Status", dataType = FieldMetaDataSupportedDataType.STRING,
            enumClass = ChargeRateStatus.class, managementSeparately = true)
    @Column(length = 100)
    private @Setter @Getter String status;

    /**
     * 版本
     */
    @FieldMetaData(position = 100, label = "Version")
    @Column(length = 100)
    private @Setter @Getter String version;

    /**
     * 备注
     */
    @FieldMetaData(position = 110, label = "Remark")
    @Column(length = 2048)
    private @Setter @Getter String remark;

    @FieldMetaData(position = 120, label = "Owner", dataType = FieldMetaDataSupportedDataType.OBJECT,
            labelField = "loginName", managementSeparately = true, autogenerated = true, hasDetail = false)
    @ManyToOne
    @JoinColumn(name = "ownerId", foreignKey = @ForeignKey(name = "fk_chargerate_useraccount_ownerid"),
            referencedColumnName = "id")
    private @Getter @Setter UserAccount owner;
}
