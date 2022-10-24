package com.example.jimmy_fturgunov.persistence.dao.common;


import com.example.jimmy_fturgunov.common.exceptions.DAOException;
import com.example.jimmy_fturgunov.persistence.model.common.IEntity;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractDAO<VIEWENTITY extends IEntity, CRUDENTITY extends IEntity, ID extends Serializable>
        implements GenericDAO<VIEWENTITY, CRUDENTITY, ID> {

    public Class<VIEWENTITY> viewEntityClass =
            (Class<VIEWENTITY>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];

    public Class<CRUDENTITY> crudClass = (Class<CRUDENTITY>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[1];

    @Autowired
    protected EntityManager entityManager;

    protected Session getSession() {
        Session session = entityManager.unwrap(Session.class);
        return session;
    }

    @Override
    public List<CRUDENTITY> findAll() throws DAOException {
        return getSession()
                .createCriteria(crudClass)
                .list();
    }


    @Override
    public VIEWENTITY findViewById(ID id) {
        return (VIEWENTITY)getSession()
                .createQuery("from " + viewEntityClass.getSimpleName() + " where id = :id")
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public CRUDENTITY findById(ID id) throws DAOException {
        return (CRUDENTITY)getSession()
                .createQuery("from " + crudClass.getSimpleName() + " where id = :id")
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public CRUDENTITY saveOrUpdate(CRUDENTITY entity) throws DAOException {
        this.getSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public CRUDENTITY insert(CRUDENTITY entity) {
        this.getSession().save(entity);
        return entity;
    }

    @Override
    public void update(CRUDENTITY entity) throws DAOException {
        this.getSession().update(entity);
    }

    @Override
    public void delete(CRUDENTITY entity) throws DAOException {
        getSession().delete(entity);
    }
}
