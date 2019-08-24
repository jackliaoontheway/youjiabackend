package com.polarj.model.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.polarj.model.FieldSpecification;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.UserAccount;

public interface EntityService<T extends GenericDbInfo, ID extends Serializable>
{

    long count();

    @Transactional
    void delete(ID id, Integer operId);

    @Transactional
    void delete(List<T> records, Integer operId);

    boolean exists(ID id);

    @Transactional
    List<T> list(int limit, String languageId);

    @Transactional
    List<T> list(String languageId);
    
    List<T> listByOwner(UserAccount owner, String languageId);

    @Transactional
    List<T> list(List<ID> ids, String languageId);

    @Transactional
    Page<T> list(Pageable p, String languageId);

    @Transactional
    T getById(ID id, String languageId);

    @Transactional
    T getById(ID id, int deepth, String languageId);

    @Transactional
    T create(T entity, Integer operId, String languageId);

    @Transactional
    List<T> create(List<T> entities, Integer operId, String languageId);

    @Transactional
    T update(ID entityId, T entityWithUpdatedInfo, Integer operId, String languageId);

    @Transactional
    List<FieldSpecification> getFieldMetaData(Class<?> clazz, UserAccount userAcc, String languageId);

    @Transactional
    Page<T> listByCriteria(T entity, String sortField, boolean desc, String languageId, Integer dataLevel);

    void setRepos(JpaRepository<T, ID> repos);

    //是不是放service是否合理一点？
    String validateEntity(T entity, Integer operId,String languageId);

    <M extends GenericDbInfo> String getFilterMsg(T entity);

    //这里添加通用方法,用于导入excel时,需要特殊处理数据
    @Transactional
    List<T> dealWithEntitys(List<T> entities);
}
