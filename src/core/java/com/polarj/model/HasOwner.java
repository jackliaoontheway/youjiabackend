package com.polarj.model;

// 业务模型实现这个接口，表明这个业务模型的数据是与主账户挂钩的。
// 在UserRestrictionModelController中使用
public interface HasOwner
{
    public UserAccount getOwner();

    public void setOwner(UserAccount owner);
}
