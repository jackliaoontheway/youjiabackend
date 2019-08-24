package com.polarj.common.utility.report;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class FieldNameAndHeaderMapping
{
    private Map<String, String> fieldNameAndHeaderMap; // fieldName -> Report Header Name

    private Map<String, String> headerAndFieldNameMap; // fieldName -> Report Header Name

    public FieldNameAndHeaderMapping()
    {
    }

    public void addFieldNameAndHeaderPair(String fieldName, String header)
    {
        if (fieldNameAndHeaderMap == null)
        {
            fieldNameAndHeaderMap = new LinkedHashMap<String, String>();
            headerAndFieldNameMap = new LinkedHashMap<String, String>();
        }
        fieldNameAndHeaderMap.put(fieldName, header);
        headerAndFieldNameMap.put(header, fieldName);
    }

    public Map<String, String> getFieldNameToHeaderMap()
    {
        if (fieldNameAndHeaderMap == null)
        {
            fieldNameAndHeaderMap = new LinkedHashMap<String, String>();
        }
        return Collections.unmodifiableMap(fieldNameAndHeaderMap);
    }

    public Map<String, String> getHeaderToFieldNameMap()
    {
        if (headerAndFieldNameMap == null)
        {
            headerAndFieldNameMap = new LinkedHashMap<String, String>();
        }
        return Collections.unmodifiableMap(headerAndFieldNameMap);
    }

}
