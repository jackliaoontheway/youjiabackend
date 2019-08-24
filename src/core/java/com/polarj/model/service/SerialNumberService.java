package com.polarj.model.service;

import com.polarj.model.service.EntityService;
import com.polarj.model.SerialNumber;

public interface SerialNumberService extends EntityService<SerialNumber, Integer>
{
    public String generateSerialNumberByModelCode(String moduleCode);

    public SerialNumber fetchSNForModule(String moduleCode);
}
