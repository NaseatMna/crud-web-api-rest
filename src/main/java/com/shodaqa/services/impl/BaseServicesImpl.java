package com.shodaqa.services.impl;

import com.shodaqa.repositories.BaseRepositories;
import com.shodaqa.helpers.Page;
import com.shodaqa.services.BaseServices;
import com.shodaqa.utils.QueryMonitor;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
@Service
public class BaseServicesImpl<T,PK extends Serializable> implements BaseServices<T,PK> {
    private BaseRepositories<T, PK> baseRepositories;


    public BaseRepositories<T, PK> getBaseRepositories() {
        return baseRepositories;
    }

    public void setBaseRepositories(BaseRepositories<T, PK> baseRepositories) {
        this.baseRepositories = baseRepositories;
    }

    @Override
    public String getEntityName() {
        return baseRepositories.getEntityName();
    }

    /**
     * {@inheritDoc}
     */
    public T load(PK id) {
        return baseRepositories.load(id);
    }

    @Override
    public T load(DetachedCriteria criteria) {
        return baseRepositories.load(criteria);
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        return baseRepositories.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> get(PK[] ids) {
        return baseRepositories.get(ids);
    }

    /**
     * {@inheritDoc}
     */
    public T get(String propertyName, Object value) {
        return baseRepositories.get(propertyName, value);
    }

    @Override
    public T get(DetachedCriteria criteria) {
        return baseRepositories.get(criteria);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> getList(String propertyName, Object value) {
        return baseRepositories.getList(propertyName, value);
    }

    @Override
    public Integer getCount() {
        return baseRepositories.getCount();
    }

    @Override
    public Integer getCount(DetachedCriteria criteria) {
        return baseRepositories.getCount(criteria);
    }

    /**
     * {@inheritDoc}
     */
    public Integer getAllCount() {
        return baseRepositories.getCount();
    }

    /**
     * {@inheritDoc}
     */
    public PK save(T entity) {
        return baseRepositories.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void saveOrUpdate(T entity) {
        baseRepositories.saveOrUpdate(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void update(T entity) {
        baseRepositories.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void merge(T entity) {
        baseRepositories.merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(T entity) {
        baseRepositories.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(PK id) {
        baseRepositories.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(PK[] ids) {
        baseRepositories.delete(ids);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(List<T> list) {
        baseRepositories.delete(list);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> list() {
        return baseRepositories.list();
    }

    @Override
    public List<T> list(DetachedCriteria criteria) {
        return baseRepositories.list(criteria);
    }

    @Override
    public Object findObject(DetachedCriteria criteria) {
        return baseRepositories.list(criteria);
    }

    @Override
    public List<T> page(DetachedCriteria criteria, Integer pageSize, Integer pageNumber) {
        return baseRepositories.page(criteria, pageSize, pageNumber);
    }

    @Override
    public Page<T> getPage(DetachedCriteria criteria, ProjectionList projectionList, Page<T> page) {
        return baseRepositories.getPage(criteria, projectionList, page);
    }

    @Override
    public Page<T> listAndPage(DetachedCriteria criteria, ProjectionList projectionList, Integer pageSize, Integer nowPage, String orderBy) {
        return baseRepositories.listAndPage(criteria, projectionList, pageSize, nowPage, orderBy);
    }

    @Override
    public Page<T> listAndPageOrder(DetachedCriteria criteria, ProjectionList projectionList, Page<T> page) {
        return baseRepositories.listAndPageOrder(criteria, projectionList, page);
    }

    protected void queryMonitor(Statistics statistics)
            throws Exception {
        long monitorId = QueryMonitor.get(statistics).start();
//		System.out.println(monitorId);
        System.out.println("Out of work here " + QueryMonitor.get(statistics)
                .stop(monitorId, "Just Test").toString());
    }

    @Override
    public long getSpeed() {
        SessionFactory session = baseRepositories.getSession().getSessionFactory();
        Statistics sessionStats = session.getStatistics();
        System.out.println("SessionState " + sessionStats.toString());
        try {
            queryMonitor(sessionStats);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
