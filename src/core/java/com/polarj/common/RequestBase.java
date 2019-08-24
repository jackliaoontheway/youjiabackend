package com.polarj.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public @ToString @EqualsAndHashCode(callSuper = false) abstract class RequestBase<T>
{
    private @Getter @Setter T data;
}
