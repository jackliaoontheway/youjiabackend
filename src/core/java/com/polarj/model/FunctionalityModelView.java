package com.polarj.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class FunctionalityModelView
{
    private @Setter @Getter Integer userRoleId;
    private @Setter @Getter Integer id;
    private @Setter @Getter Integer positionSn;
    private @Setter @Getter String label;
    private @Setter @Getter boolean selected;
    private @Setter @Getter List<FunctionalityModelView> subMenus;
}
