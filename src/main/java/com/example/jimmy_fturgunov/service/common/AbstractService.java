package com.example.jimmy_fturgunov.service.common;

import com.example.jimmy_fturgunov.persistence.dao.common.GenericDAO;
import com.example.jimmy_fturgunov.persistence.model.common.IEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractService<VIEWENTITY extends IEntity, CRUDENTITY extends IEntity, ID extends Serializable>
        implements IService<VIEWENTITY, CRUDENTITY, ID> {

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CRUDENTITY> findAll() {
        return getServiceDAO().findAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public VIEWENTITY findViewById(ID id) {
        return getServiceDAO().findViewById(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CRUDENTITY findById(ID id) {
        return getServiceDAO().findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CRUDENTITY saveOrUpdate(CRUDENTITY entity) {
        return getServiceDAO().saveOrUpdate(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CRUDENTITY delete(ID id) {
        CRUDENTITY entity = getServiceDAO().findById(id);
        getServiceDAO().delete(entity);
        return entity;
    }

    protected abstract GenericDAO<VIEWENTITY, CRUDENTITY, ID> getServiceDAO();
}
