package com.shodaqa.repositories.RepositoriesImpl;

import com.shodaqa.repositories.BaseRepositories;
import com.alibaba.fastjson.JSONObject;
import com.shodaqa.helpers.Page;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Naseat_PC on 9/9/2017.
 */
@Transactional
@Repository
public class BaseRepositoriesImpl<T, PK extends Serializable> implements BaseRepositories<T,PK> {
    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> entityClass;
    @SuppressWarnings({"unchecked"})
    public BaseRepositoriesImpl(){
        this.entityClass = null;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
            this.entityClass = (Class<T>) parameterizedType[0];
        }
    }
    public String getEntityName() {
        return this.entityClass.getName();
    }

    /**
     * {@inheritDoc}
     */
    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * {@inheritDoc}
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * {@inheritDoc}
     */
    public Criteria getCriteria() {
        return getSession().createCriteria(entityClass);
    }

    /**
     * {@inheritDoc}
     */
    public T load(PK id) {
        return getSession().load(this.entityClass, id);
    }

    @Override
    public T load(DetachedCriteria criteria) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        return getSession().get(this.entityClass, id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> get(PK[] ids) {
        return getSession().createQuery("from " + entityClass.getName() +
                " as model where model.id in (:ids)").setParameterList("ids", ids).list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(String propertyName, Object value) {
        return (T) getSession().createQuery("from " + entityClass.getName() +
                " as model where model." + propertyName + " = ?").setParameter(0, value).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    public T get(DetachedCriteria criteria) {
        List<T> list = list(criteria);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getList(String propertyName, Object value) {
        return getSession().createQuery("from " + entityClass.getName() +
                " as model where model." + propertyName + " = ?").setParameter(0, value).list();
    }

    /**
     * {@inheritDoc}
     */
    public Integer getCount() {
        return Integer.parseInt(getCriteria().setProjection(Projections.rowCount()).uniqueResult().toString());
    }

    /**
     * {@inheritDoc}
     */
    public Integer getCount(DetachedCriteria criteria) {
        return Integer.parseInt(criteria.setProjection(Projections.rowCount())
                .getExecutableCriteria(getSession())
                .uniqueResult()
                .toString());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public PK save(T entity) {
        return (PK) getSession().save(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void update(T entity) {
        getSession().update(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void merge(T entity) {
        getSession().merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(T entity) {
        getSession().delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(PK id) {
        getSession().delete(load(id));
    }

    /**
     * {@inheritDoc}
     */
    public void delete(PK[] ids) {
        for (PK id : ids) {
            delete(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete(List<T> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(T -> {
                getSession().delete(T);
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> list() {
        return getCriteria().list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> list(DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(getSession()).list();
    }

    /**
     * {@inheritDoc}
     */
    public Object findObject(DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(getSession()).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> page(DetachedCriteria criteria, Integer pageSize, Integer pageNumber) {
        return criteria.getExecutableCriteria(getSession()).setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    /**
     * {@inheritDoc}
     */
    public Page<T> getPage(DetachedCriteria criteria, ProjectionList projectionList, Page<T> page) {
        if (page.getKeyWords() != null && page.getProperty() != null && "".equals(page.getKeyWords().trim())) {
            criteria.add(Restrictions.like(page.getProperty(), page.getKeyWords(), page.getMatchMode()));
        }
        page.setTotalCount(getCount(criteria));
        criteria.setProjection(projectionList);
        if (page.getOrderBy() != null) {
            if (page.getOrderType().equals(Page.OrderType.asc)) {
                criteria.addOrder(Order.asc(page.getOrderBy()));
            } else {
                criteria.addOrder(Order.desc(page.getOrderBy()));
            }
        }
//		DetachedCriteria criteria1 = DetachedCriteria.forClass(User.class);
//		criteria1.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        //criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if(projectionList != null){
            criteria.setResultTransformer(Transformers.aliasToBean(JSONObject.class));
        }else {
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        }
//		criteria.createCriteria(criteria1.getAlias());
//		criteria.setProjection(Projections.projectionList()
//				.add( Projections.distinct(Projections.property("id"))));
        page.setList(page(criteria, page.getPageSize(), page.getNowPage()));
        return page;
    }

    @Override
    public Page<T> listAndPage(DetachedCriteria criteria, ProjectionList projectionList, Integer pageSize, Integer nowPage, String orderBy) {
        Page<T> page = new Page<T>();
        page.setOrderBy(orderBy);
        page.setPageSize(pageSize);
        page.setNowPage(nowPage);
        return this.getPage(criteria, projectionList, page);
    }

    @Override
    public Page<T> listAndPageOrder(DetachedCriteria criteria, ProjectionList projectionList, Page<T> page) {
        return this.getPage(criteria, projectionList, page);
    }
}
