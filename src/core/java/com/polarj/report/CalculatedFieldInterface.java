package com.polarj.report;

import com.polarj.model.FieldSpecification;;

public interface CalculatedFieldInterface
{
    public FieldSpecification generateFieldInfo(CalculatedFieldCriteria cri);
}
