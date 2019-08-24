package com.polarj.model.service.impl;

import org.springframework.stereotype.Service;

import com.polarj.model.service.impl.EntityServiceImpl;
import com.polarj.model.OperationInfo;
import com.polarj.model.service.OperationInfoService;

@Service
public class OperationInfoServiceImpl extends EntityServiceImpl<OperationInfo, Integer>
        implements OperationInfoService
{
}
