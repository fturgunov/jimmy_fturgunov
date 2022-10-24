package com.example.jimmy_fturgunov.service.common;

import com.example.jimmy_fturgunov.persistence.model.common.IEntity;

import java.io.Serializable;
import java.util.List;

public interface IService<VIEWENTITY extends IEntity, CRUDENTITY extends IEntity, ID extends Serializable> {

    List<CRUDENTITY> findAll();

    VIEWENTITY findViewById(ID id);

    CRUDENTITY findById(ID id);

    CRUDENTITY saveOrUpdate(CRUDENTITY entity);

    CRUDENTITY delete(ID id);
}
