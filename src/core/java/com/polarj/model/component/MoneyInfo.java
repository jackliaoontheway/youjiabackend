package com.polarj.model.component;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

import com.polarj.common.CommonConstant;
import com.polarj.model.annotation.FieldMetaData;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 描述金额信息，在数据库中展开保存
@Embeddable
public @ToString class MoneyInfo implements DecodeFromFormatter, Serializable
{
    private static final long serialVersionUID = -6549777139221938437L;

    public MoneyInfo()
    {
        this(CommonConstant.defaultCurrencyCode, 0.0);
    }

    public MoneyInfo(String currency, double amount)
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
    private @Getter @Setter Double amount;

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
            amount = Double.parseDouble(valueString);
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
                amount = Double.parseDouble(valueString.substring(currencyPos + 3));
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
