package com.polarj.common.web.model;

import com.polarj.common.RequestBase;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public @ToString @EqualsAndHashCode(callSuper = false) class ClientRequest<T> extends RequestBase<T>
{
    private @Getter @Setter String nonceToken; // against CRSF
    
    private @Getter @Setter String apiToken;
}
