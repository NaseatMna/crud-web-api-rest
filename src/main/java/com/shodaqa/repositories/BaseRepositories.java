package com.shodaqa.repositories;

import com.shodaqa.helpers.Page;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
public  interface BaseRepositories <T,PK extends Serializable> {
    void setEntityClass(Class<T> entityClass);

    Session getSession();

    Criteria getCriteria();

    String getEntityName();

    T load(PK id);

    T load(DetachedCriteria criteria);

    T get(PK id);

    List<T> get(PK[] ids);

    T get(String propertyName, Object value);

    T get(DetachedCriteria criteria);

    List<T> getList(String propertyName, Object value);

    Integer getCount();

    Integer getCount(DetachedCriteria criteria);

    PK save(T entity);

    void saveOrUpdate(T entity);

    void update(T entity);

    void merge(T entity);

    void delete(T entity);

    void delete(PK id);

    void delete(PK[] ids);

    void delete(List<T> list);

    List<T> list();

    List<T> list(DetachedCriteria criteria);

    Object findObject(DetachedCriteria criteria);

    List<T> page(DetachedCriteria criteria, Integer pageSize, Integer pageNumber);

    Page<T> getPage(DetachedCriteria criteria, ProjectionList projectionList, Page<T> page);

    Page<T> listAndPage(DetachedCriteria criteria, ProjectionList projectionList, Integer pageSize, Integer nowPage, String orderBy);

    Page<T> listAndPageOrder(DetachedCriteria criteria, ProjectionList projectionList, Page<T> page);

}
