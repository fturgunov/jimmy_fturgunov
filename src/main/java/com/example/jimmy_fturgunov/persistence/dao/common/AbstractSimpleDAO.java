package com.example.jimmy_fturgunov.persistence.dao.common;

import com.example.jimmy_fturgunov.persistence.model.common.IEntity;
import org.hibernate.Session;

import java.io.Serializable;

public abstract class AbstractSimpleDAO<VIEWENTITY extends IEntity, CRUDENTITY extends IEntity, ID extends Serializable>
        extends AbstractDAO<VIEWENTITY, CRUDENTITY, ID> {

    @Override
    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}