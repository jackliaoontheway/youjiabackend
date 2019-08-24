package com.hwcargo.service.impl;

import java.util.List;

import com.hwcargo.model.CartonData;

import lombok.Getter;
import lombok.Setter;

public class CartonDataPickResponse
{
    private @Setter @Getter String errorMsg;
    
    private @Setter @Getter List<CartonData> cartonDataList;
}
