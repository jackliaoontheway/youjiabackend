package com.dragoncargo.general.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

import com.polarj.common.CommonConstant;
import com.polarj.model.annotation.FieldMetaData;
import com.polarj.model.component.DecodeFromFormatter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
public @ToString class FeeInfo implements DecodeFromFormatter, Serializable
{

    private static final long serialVersionUID = -6549777139221938437L;

    public FeeInfo()
    {
        this(CommonConstant.defaultCurrencyCode, new BigDecimal(0.0));
    }

    public FeeInfo(String currency, BigDecimal amount)
    {
        this.currency = currency;
        this.amount = amount;
    }

    // 货币代码 ISO 4217
    @FieldMetaData(position = 10, label = "Currency")
    @Column(length = 16)
    private @Getter @Setter String currency;

    @FieldMetaData(position = 20, label = "Amount")
    @Column
    private @Getter @Setter BigDecimal amount;

    @Override
    public void setFieldValue(String formatter, String valueString)
    {
        // 目前暂时假设formatter是固定的${currency}${amount}
        // 同时假设使用的是3位字母的货币代码
        // FIXME
        if (StringUtils.isEmpty(formatter) || StringUtils.isEmpty(valueString))
        {
            return;
        }

        if (Character.isDigit(valueString.charAt(0)))
        {// 纯数字的串，使用系统缺省的货币
            currency = CommonConstant.defaultCurrencyCode;
            amount = BigDecimal.valueOf(Double.parseDouble(valueString));
        }
        else
        {
            int currencyPos = formatter.indexOf("${currency}");
            int amountPos = formatter.indexOf("${amount}");
            if (amountPos == -1 || currencyPos == -1)
            {
                return;
            }
            if (currencyPos < amountPos)
            {
                currency = valueString.substring(currencyPos, 3);
                amount = BigDecimal.valueOf(Double.parseDouble(valueString.substring(currencyPos + 3)));
            }
        }
    }

    @Override
    public String toString(String formatter)
    {
        // 目前暂时假设formatter是固定的${currency}${amount}
        return String.format("%s%.2f", currency, amount);
    }

    public boolean valid()
    {
        if (StringUtils.isEmpty(currency))
        {
            currency = CommonConstant.defaultCurrencyCode;
        }
        if (amount == null)
            return false;
        return true;
    }

}
